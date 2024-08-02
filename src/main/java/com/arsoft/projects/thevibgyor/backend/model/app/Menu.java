package com.arsoft.projects.thevibgyor.backend.model.app;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "menu")
public class Menu {
    @Field("id")
    private int id;
    @Field("display_name")
    private String displayName;
    @Field("component_name")
    private String componentName;
    @Field("sub_menu_ids")
    private List<Integer> subMenuIds;
    @Setter
    private List<Menu> subMenu;
}
