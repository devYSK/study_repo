package com.ys.jpa_example.idgenerator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<TimeEntity, String> {

}
