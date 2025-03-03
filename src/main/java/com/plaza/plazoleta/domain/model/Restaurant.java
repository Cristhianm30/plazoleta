package com.plaza.plazoleta.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nit", nullable = false)
    private String nit;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "cell_phone", nullable = false)
    private String cellPhone;

    @Column(name = "url_logo", nullable = false)
    private String urlLogo;

    @Column(name = "user_id", nullable = false)
    private Long userId;

}
