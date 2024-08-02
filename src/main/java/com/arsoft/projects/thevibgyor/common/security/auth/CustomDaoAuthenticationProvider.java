package com.arsoft.projects.thevibgyor.common.security.auth;

import com.arsoft.projects.thevibgyor.common.security.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private Hashing hashing;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String hash = hashing.getSha256Hash((String) authentication.getCredentials());
        UsernamePasswordAuthenticationToken authenticationNew = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), hash, authentication.getAuthorities());
        return super.authenticate(authenticationNew);
    }
}
