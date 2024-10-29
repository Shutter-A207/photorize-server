package com.shutter.photorize.domain.pose.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.pose.entity.Pose;

@Repository
public interface PoseRepository extends JpaRepository<Pose, Long> {
}
