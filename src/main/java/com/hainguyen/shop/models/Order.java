package com.hainguyen.shop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "fullname")
    private String fullName;
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;
    private String note;
    private Float totalMoney;
    private String shippingMethod;
    private String shippingAddress;
    private String paymentMethod;

    // Exclude inside OrderDto
    private LocalDate orderDate;
    private String status;
    private LocalDate shippingDate;
    private String trackingNumber;
    private boolean active;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;


    public void addOrderDetail(OrderDetail detail) {
        if (orderDetails == null) {
            orderDetails = new ArrayList<>();
        }
        orderDetails.add(detail);
        detail.setOrder(this);
    }
}
