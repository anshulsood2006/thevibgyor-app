package com.arsoft.projects.thevibgyor.backend.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class GenericResponseInfo<T> {
    private boolean multi;
    private String key;
    private int count;
    private T data;
}
