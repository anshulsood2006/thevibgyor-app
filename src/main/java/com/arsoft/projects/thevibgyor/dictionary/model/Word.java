package com.arsoft.projects.thevibgyor.dictionary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class Word {
    private Input input;
    private List<Meaning> meanings;
}
