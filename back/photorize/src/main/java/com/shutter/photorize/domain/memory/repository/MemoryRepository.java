package com.shutter.photorize.domain.memory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.memory.entity.Memory;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {
}
