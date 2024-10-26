package com.example.back.repository;

import com.example.back.entity.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {
    UserDetails findByEmail(String email);
}
