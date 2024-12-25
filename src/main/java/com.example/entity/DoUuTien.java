package com.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "DoUuTien")
public class DoUuTien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int madouutien;

    @Column(nullable = false)
    private String tendouutien;

    public int getMadouutien() {
        return madouutien;
    }

    public void setMadouutien(int madouutien) {
        this.madouutien = madouutien;
    }

    public String getTendouutien() {
        return tendouutien;
    }

    public void setTendouutien(String tendouutien) {
        this.tendouutien = tendouutien;
    }
}
