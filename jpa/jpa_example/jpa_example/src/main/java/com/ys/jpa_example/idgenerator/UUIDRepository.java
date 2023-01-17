package com.ys.jpa_example.idgenerator;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UUIDRepository extends JpaRepository<UUIDEntity, String> {

}
