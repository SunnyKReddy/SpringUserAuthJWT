package com.learnleadgrow.springauth.userauthservice.repository;

import com.learnleadgrow.springauth.userauthservice.model.Session;
import com.learnleadgrow.springauth.userauthservice.model.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByTokenAndUser_Id(String token, Long userId);
    List<Session> findByUser_Id(Long userId);

    List<Session> findByUser_IdAndSessionStatus(Long userId, SessionStatus sessionStatus);
}
