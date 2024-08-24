package com.arsoft.projects.thevibgyor.shopping.entity;

import static com.arsoft.projects.thevibgyor.shopping.constant.StaticNames.CREATED_AT;
import static com.arsoft.projects.thevibgyor.shopping.constant.StaticNames.IS_ACTIVE;
import static com.arsoft.projects.thevibgyor.shopping.constant.StaticNames.IS_DELETED;
import static com.arsoft.projects.thevibgyor.shopping.constant.StaticNames.UPDATED_AT;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AbstractPersistable {

	@Field(IS_DELETED)
	private Boolean isDeleted = Boolean.FALSE;
	
	@Field(IS_ACTIVE)
	private Boolean isActive = Boolean.TRUE;
	
	@Field(CREATED_AT)
	@CreatedDate
	private LocalDateTime createdAt;
	
	@Field(UPDATED_AT)
	private LocalDateTime updatedAt;
	
}
