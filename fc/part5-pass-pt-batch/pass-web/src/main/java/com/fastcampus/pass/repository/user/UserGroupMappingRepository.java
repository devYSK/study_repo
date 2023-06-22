package com.fastcampus.pass.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserGroupMappingRepository extends JpaRepository<UserGroupMappingEntity, Integer> {

    @Query("select distinct u.userGroupId " +
            "from UserGroupMappingEntity u " +
            "order by u.userGroupId")
    List<String> findDistinctUserGroupId();
}
