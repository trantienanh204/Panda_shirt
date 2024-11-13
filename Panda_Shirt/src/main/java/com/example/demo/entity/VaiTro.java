package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "VAI_TRO")
public class VaiTro {
    @Id
    @Column(name = "ID")
    private Integer idVaiTro;

    @Column(name = "TEN_VAI_TRO")
    private String tenVaiTro;


}
