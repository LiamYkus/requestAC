package com.example.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "YeuCau")
public class YeuCau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mayeucau;

    @Column(nullable = false)
    private String tieude;

    private String noidung;

    @Temporal(TemporalType.DATE)
    private Date ngaygui;

    @ManyToOne
    @JoinColumn(name = "madouutien")
    private DoUuTien doUuTien;

    @ManyToOne
    @JoinColumn(name = "manv_gui")
    private NhanVien manvGui;

    @ManyToOne
    @JoinColumn(name = "manv_xuly", referencedColumnName = "username")
    private NhanVien nhanVienXuLy;

    public int getMayeucau() {
        return mayeucau;
    }

    public void setMayeucau(int mayeucau) {
        this.mayeucau = mayeucau;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public Date getNgaygui() {
        return ngaygui;
    }

    public void setNgaygui(Date ngaygui) {
        this.ngaygui = ngaygui;
    }

    public DoUuTien getDoUuTien() {
        return doUuTien;
    }

    public void setDoUuTien(DoUuTien doUuTien) {
        this.doUuTien = doUuTien;
    }

    public NhanVien getManvGui() {
        return manvGui;
    }

    public void setManvGui(NhanVien manvGui) {
        this.manvGui = manvGui;
    }

    public NhanVien getManvXuly() {
        return nhanVienXuLy;
    }

    public void setManvXuly(NhanVien manvXuly) {
        this.nhanVienXuLy = manvXuly;
    }
}
