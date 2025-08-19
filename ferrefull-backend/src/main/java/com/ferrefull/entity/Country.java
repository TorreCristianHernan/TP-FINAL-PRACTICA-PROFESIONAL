package com.ferrefull.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "countries")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Country extends BaseEntityAudit {
    private String name;
}
