package com.hainguyen.shop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hainguyen.shop.models.listenner.ProductListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder
@EntityListeners(ProductListener.class)
public class Product extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Float price;
    private String thumbnail;
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<ProductImage> productImages;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<Comment> comments;

}
