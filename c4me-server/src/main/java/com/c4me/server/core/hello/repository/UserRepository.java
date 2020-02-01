package com.c4me.server.core.hello.repository;

import com.c4me.server.entities.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<TestEntity, Long> {
}
