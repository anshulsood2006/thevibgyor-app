package com.arsoft.projects.thevibgyor.backend.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class GenericResponse<T> {
    private Header header;
    private GenericResponseInfo<T> response;
}
