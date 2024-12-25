package com.example.utils;

import com.example.entity.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.example.entity.NhanVien;

import java.util.Date;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Cấu hình Hibernate
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

            // Tạo tài khoản admin mặc định
            createDefaultAdmin();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static void createDefaultAdmin() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Kiểm tra xem đã có admin chưa
            String hql = "FROM NhanVien WHERE loaitaikhoan = :role";
            NhanVien admin = session.createQuery(hql, NhanVien.class)
                    .setParameter("role", Role.ADMIN)
                    .uniqueResult();

            if (admin == null) {
                // Mã hóa mật khẩu
                String hashedPassword = PasswordUtil.hashPassword("admin123");

                // Tạo tài khoản admin mặc định
                NhanVien adminAccount = new NhanVien(
                        "admin",                        // Tên đăng nhập
                        hashedPassword,                 // Mật khẩu đã mã hóa
                        "Admin User",                   // Họ tên
                        new Date(),                     // Ngày sinh
                        true,                           // Kích hoạt tài khoản
                        null,                           // Hình ảnh
                        Role.ADMIN                      // Vai trò admin
                );

                session.save(adminAccount);
                System.out.println("Tài khoản admin mặc định đã được tạo.");
            } else {
                System.out.println("Tài khoản admin đã tồn tại.");
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
