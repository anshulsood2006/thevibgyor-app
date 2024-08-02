package com.arsoft.projects.thevibgyor.dictionary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Meaning {
    private Language language;
    private String meaning;
}
