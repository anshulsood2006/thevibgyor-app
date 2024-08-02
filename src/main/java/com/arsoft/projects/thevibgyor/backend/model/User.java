package com.arsoft.projects.thevibgyor.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class User {
    @Field("id")
    private int id;
    @Field("username")
    private String username;
    @Field("user_id")
    private String userId;
    @Field("email")
    private String email;
    @Field("role_ids")
    private List<Integer> roleIds;
    @Field("password")
    private String password;
    @Setter
    private List<Role> roles;
}
