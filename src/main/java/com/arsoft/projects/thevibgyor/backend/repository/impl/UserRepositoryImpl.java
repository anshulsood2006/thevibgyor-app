package com.arsoft.projects.thevibgyor.backend.repository.impl;

import com.arsoft.projects.thevibgyor.backend.model.Role;
import com.arsoft.projects.thevibgyor.backend.model.User;
import com.arsoft.projects.thevibgyor.backend.repository.UserRepository;
import com.arsoft.projects.thevibgyor.common.security.hash.Hashing;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static com.arsoft.projects.thevibgyor.common.constant.Collection.COLLECTION_USERS;
import static com.arsoft.projects.thevibgyor.common.constant.Collection.COLLECTION_USER_ROLES;

public class UserRepositoryImpl implements UserRepository {

    /**
     * //This is needed, otherwise credentials won't match
     * String encodedPassword = passwordEncoder.encode(hashing.getSha256Hash("password"));
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private Hashing hashing;

    @Override
    public User getUser(String username) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection(COLLECTION_USERS);
        Document query = new Document("username", username);
        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            if (!cursor.hasNext()) {
                throw new Exception(String.format("No users found with the username '%s'", username));
            }
            Document document = cursor.next();
            if (cursor.hasNext()) {
                throw new Exception(String.format("More than one users found with the same username '%s'", username));
            }
            User user = mongoTemplate.getConverter().read(User.class, document);
            user.setRoles(getListOfRoles(user.getRoleIds()));
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<Role> getListOfRoles(List<Integer> roleIds) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> subCollection = database.getCollection(COLLECTION_USER_ROLES);
        return roleIds.stream().map(id -> {
            Document subQuery = new Document("id", id);
            Document subQueryDoc = subCollection.find(subQuery).first();
            assert subQueryDoc != null;
            return mongoTemplate.getConverter().read(Role.class, subQueryDoc);
        }).toList();
    }

    @Override
    public List<User> getUsers() {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection(COLLECTION_USERS);
        FindIterable<Document> documents = collection.find();
        List<User> users = new ArrayList<>();
        documents.forEach(document -> {
            users.add(User.builder()
                    .id(document.getInteger("id"))
                    .username(document.getString("username"))
                    .email(document.getString("email"))
                    .userId(document.getString("user_id"))
                    .roleIds(document.getList("role_ids", Integer.class))
                    .password("*****************")
                    .roles(getListOfRoles(document.getList("role_ids", Integer.class)))
                    .build());
        });
        return users;
    }

    @Override
    public User createUser(User user) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection(COLLECTION_USERS);
        Document userDocument = new Document("id", user.getId())
                .append("username", user.getUsername())
                .append("email", user.getEmail())
                .append("user_id", user.getUserId())
                .append("role_ids", user.getRoleIds())
                .append("password", passwordEncoder.encode(hashing.getSha256Hash(user.getPassword())));
        collection.insertOne(userDocument);
        user.setRoles(getListOfRoles(user.getRoleIds()));
        return user;
    }
}