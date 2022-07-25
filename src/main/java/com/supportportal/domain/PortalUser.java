package com.supportportal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@Entity
@NoArgsConstructor
public class PortalUser implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false)
  private Long id;
  private String userId;
  private String firstName;
  private String lastName;
  private String userName;
  private String password;
  private String email;
  private String profileImageUrl;
  private Date lastLoginDate;
  private Date lastLoginDateDisplay;
  private Date joinDate;
  private String[] roles; //ROLE_USER { read, edit }, ROLE_ADMIN {delete}
  private String[] authorities;
  private boolean isActive;
  private boolean isNotLocked;
}
