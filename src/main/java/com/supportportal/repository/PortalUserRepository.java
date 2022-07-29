package com.supportportal.repository;

import com.supportportal.domain.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortalUserRepository extends JpaRepository<PortalUser, Long> {
  Optional<PortalUser> findPortalUserByUsername(String username);
  Optional<PortalUser> findPortalUserByEmail(String email);
}
