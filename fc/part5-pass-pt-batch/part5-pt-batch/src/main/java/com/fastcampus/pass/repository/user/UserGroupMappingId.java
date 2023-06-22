package com.fastcampus.pass.repository.user;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupMappingId implements Serializable {
	private String userGroupId;
	private String userId;
}