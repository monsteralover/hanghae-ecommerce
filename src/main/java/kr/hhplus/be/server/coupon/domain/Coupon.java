package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.BaseEntity;
import lombok.Getter;

import java.time.LocalDate;

@Entity
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private String couponName;

    @Getter
    private int discountAmount;

    private int totalQuantity;

    private int remainingQuantity;

    @Getter
    private LocalDate expireDate;

}
