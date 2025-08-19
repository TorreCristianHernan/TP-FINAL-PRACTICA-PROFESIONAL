package com.ferrefull.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Customer extends BaseEntityAudit {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String location;
    private String province;
    private String bussinessName;
    private String cuit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

}
