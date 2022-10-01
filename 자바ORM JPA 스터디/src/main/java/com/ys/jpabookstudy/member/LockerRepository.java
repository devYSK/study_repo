package com.ys.jpabookstudy.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

/**
 * @author : ysk
 */
public interface LockerRepository extends JpaRepository<Locker, Long> {

}
