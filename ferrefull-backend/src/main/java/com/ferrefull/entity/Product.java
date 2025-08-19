package com.ferrefull.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntityAudit {
    private String sku;
    private String name;
    private String description;
    private Integer stock;
}
