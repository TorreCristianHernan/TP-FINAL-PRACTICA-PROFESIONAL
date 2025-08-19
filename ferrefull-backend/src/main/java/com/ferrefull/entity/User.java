package com.ferrefull.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntityAudit {
    private String email;
//    @JsonIgnore
    private String password;
    private String roles;
    private String firstName;
    private String lastName;
    private String type;
    private String phone;
}
