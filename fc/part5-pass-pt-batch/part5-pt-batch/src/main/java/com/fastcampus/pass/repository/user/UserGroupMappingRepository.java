package com.fastcampus.pass.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupMappingRepository extends JpaRepository<UserGroupMappingEntity, Integer> {
	List<UserGroupMappingEntity> findByUserGroupId(String userGroupId);
}