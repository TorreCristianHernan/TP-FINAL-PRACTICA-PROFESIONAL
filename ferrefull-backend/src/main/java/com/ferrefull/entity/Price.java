package com.ferrefull.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "prices")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Price extends BaseEntityAudit {
    private Double salePrice;
    private Date effectiveDate;
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Product product;
}
