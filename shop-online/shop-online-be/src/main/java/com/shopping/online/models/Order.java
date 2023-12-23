package com.shopping.online.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(length = 300)
    private String note;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "total_money", nullable = false)
    private Float totalMoney;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "shipping_method", nullable = false)
    private String shippingMethod;

    @Column(name = "shipping_date",nullable = false)
    private LocalDate shippingDate;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

}
