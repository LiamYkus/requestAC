package com.example.dao;

import com.example.entity.NhanVien;
import com.example.utils.HibernateUtil;
import org.hibernate.Session;

public class NhanVienDAO extends GenericDAOImpl<NhanVien, String> {
    public NhanVienDAO() {
        super(NhanVien.class);
    }
    public NhanVien findByUsernameAndPassword(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM NhanVien WHERE username = :username AND password = :password AND quyen = 3", NhanVien.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
        }
    }
}
