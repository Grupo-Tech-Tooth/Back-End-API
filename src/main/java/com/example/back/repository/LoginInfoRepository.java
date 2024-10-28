package com.example.back.repository;

import com.example.back.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
    UserDetails findByEmail(String email);

    @Query("SELECT l FROM LoginInfo l WHERE l.email = :email")
    Optional<LoginInfo> buscarPorEmail(String email);
}
