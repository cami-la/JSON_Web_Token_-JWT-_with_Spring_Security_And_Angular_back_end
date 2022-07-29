package com.supportportal.service.impl;

import com.supportportal.domain.PortalUser;
import com.supportportal.domain.UserPrincipal;
import com.supportportal.repository.PortalUserRepository;
import com.supportportal.service.PortalUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Qualifier("UserDetailsService")
@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class PortalUserServiceImpl implements PortalUserService, UserDetailsService {
  private final PortalUserRepository portalUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    PortalUser portalUser = portalUserRepository.findPortalUserByUsername(username).orElseThrow(
        () -> {
          log.error("User not found by username: {}", username);
          throw new UsernameNotFoundException("User not found by username: " + username);
        }
    );
    portalUser.setLastLoginDateDisplay(portalUser.getLastLoginDate());
    portalUser.setLastLoginDate(new Date());
    portalUserRepository.save(portalUser);
    UserPrincipal userPrincipal = new UserPrincipal(portalUser);
    log.info("Returning found user by username {}", username);
    return userPrincipal;
  }
}
