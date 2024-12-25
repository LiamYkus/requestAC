package com.example.entity;

import com.example.servlet.RoleConverter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NhanVien")
public class NhanVien {
    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    private String hoten;

    @Temporal(TemporalType.DATE)
    private Date ngaysinh;

    private boolean kichhoat;

    private String hinhanh;

    public String getUsername() {
        return username;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "loaitaikhoan", nullable = false)
    private Role loaitaikhoan;

    public NhanVien() {
    }

    public NhanVien(String username, String password, String hoten, Date ngaysinh, boolean kichhoat, String hinhanh, Role loaitaikhoan) {
        this.username = username;
        this.password = password;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.kichhoat = kichhoat;
        this.hinhanh = hinhanh;
        this.loaitaikhoan = loaitaikhoan;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public Date getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(Date ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public boolean isKichhoat() {
        return kichhoat;
    }

    public void setKichhoat(boolean kichhoat) {
        this.kichhoat = kichhoat;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public Role getLoaitaikhoan() {
        return loaitaikhoan;
    }

    public void setLoaitaikhoan(Role loaitaikhoan) {
        this.loaitaikhoan = loaitaikhoan;
    }
}
