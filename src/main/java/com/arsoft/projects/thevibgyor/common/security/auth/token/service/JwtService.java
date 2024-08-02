package com.arsoft.projects.thevibgyor.common.security.auth.token.service;

import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.backend.model.User;
import com.arsoft.projects.thevibgyor.common.util.Base64Util;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.arsoft.projects.thevibgyor.common.constant.Collection.COLLECTION_USER_ROLES;

@Component
@Slf4j
public class JwtService {

    //Should be of length which is a multiple of four
    private static final String SECRET_KEY = "MYSECRETKEYWHICHISOFATLEASTBITINSIZEMYSECRETKEYWHICHISOFATLEASTBITINSIZE";
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(Base64Util.getDecodedString(SECRET_KEY));
    private static final String issuer = "MY_AUTH_APP";
    @Autowired
    private MongoTemplate mongoTemplate;

    public String generateAccessToken(User user, String ttlInMillis) {
        long ttl = Long.parseLong(ttlInMillis);
        if (ttl < 300000) {
            log.info("Setting ttl for the token to " + 300000 + " ms.");
            ttl = 300000;
        }
        log.info(String.format("Creating token for user %s and ttl %s", user, ttl));
        return Jwts.builder()
                .subject(String.format("%s", user.getUserId()))
                .issuer(issuer)
                .claim("roles", user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toList()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ttl))
                .signWith(SIGNING_KEY, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        log.error("Claims values are " + String.valueOf(claims));
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(SIGNING_KEY).build().parseSignedClaims(token).getPayload();
    }

    public List<Role> getRoles(String token) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection(COLLECTION_USER_ROLES);
        Claims claims = extractAllClaims(token);
        List<?> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(role -> {
                    Document query = new Document("role_name", role);
                    try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
                        if (!cursor.hasNext()) {
                            throw new Exception(String.format("No role found with the role name '%s'", role));
                        }
                        Document document = cursor.next();
                        if (cursor.hasNext()) {
                            throw new Exception(String.format("More than one roles found with the same name '%s'", role));
                        }
                        return mongoTemplate.getConverter().read(Role.class, document);
                    } catch (Exception e) {
                        throw new RuntimeException(String.format("No role found with the role name '%s'", role));
                    }
                })
                .collect(Collectors.toList());
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isValidToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception ex) {
            log.error("Exception occurred while validating the token. Cause: " + ex.getMessage());
            return false;
        }
    }
}
