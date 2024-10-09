package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "USER_ROLE",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NHANVIEN_ID","ROLE_ID"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor

@SuppressWarnings("serial")
public class UserRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "NHANVIEN_ID")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;
}
