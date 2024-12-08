package com.rip.RIP_Project.repository;


import com.rip.RIP_Project.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, Long> {
    CustomUser findByUsername(String username);
}
