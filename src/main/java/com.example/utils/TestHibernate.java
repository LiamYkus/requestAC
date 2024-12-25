package com.example.utils;

import org.hibernate.SessionFactory;

public class TestHibernate {
    public static void main(String[] args) {
        try {
            // Lấy SessionFactory từ HibernateUtil
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

            // Kiểm tra xem SessionFactory có được cấu hình thành công không
            System.out.println("Hibernate SessionFactory is configured successfully!");

            // Đóng SessionFactory sau khi kiểm tra
            sessionFactory.close();
        } catch (Exception e) {
            // In lỗi nếu có vấn đề xảy ra
            e.printStackTrace();
        }
    }
}
