package com.shutter.photorize.domain.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shutter.photorize.domain.alarm.entity.FCMToken;

@Repository
public interface FCMRepository extends JpaRepository<FCMToken, Long> {

}
