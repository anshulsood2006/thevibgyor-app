package com.arsoft.projects.thevibgyor.common.security.auth.token.model;

import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse {
    private Header header;
    private String token;
    private Error error;
}
