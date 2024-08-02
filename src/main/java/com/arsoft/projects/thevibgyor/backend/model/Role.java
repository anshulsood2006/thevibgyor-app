package com.arsoft.projects.thevibgyor.backend.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Role {
    @Field("id")
    private int id;
    @Field("role_name")
    private String roleName;
}
