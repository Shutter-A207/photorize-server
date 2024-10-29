package com.shutter.photorize.domain.pose.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shutter.photorize.domain.pose.entity.Pose;

public interface PoseRepository extends JpaRepository<Pose, Long> {
}
