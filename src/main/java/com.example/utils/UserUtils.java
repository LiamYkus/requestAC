package com.example.utils;

import com.example.entity.NhanVien;
import com.example.utils.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.http.HttpSession;

public class UserUtils {

    public static NhanVien getLoggedInUser(HttpSession session) {
        String username = (String) session.getAttribute("username");

        if (username != null) {
            try (Session hibernateSession = HibernateUtil.getSessionFactory().openSession()) {
                return hibernateSession.get(NhanVien.class, username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
