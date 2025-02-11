package com.lighthouse.DTOs;

import com.lighthouse.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRoles role;
    private String createdBy;
    private Date createdOn;
    private String lastUpdatedBy;
    private Date lastUpdatedOn;
}
