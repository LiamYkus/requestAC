package com.example.dao;

import com.example.entity.YeuCau;
import com.example.utils.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class YeuCauDAO extends GenericDAOImpl<YeuCau, Integer> {
    public YeuCauDAO() {
        super(YeuCau.class);
    }

    public List<YeuCau> findByManvXuly(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    "FROM YeuCau yc WHERE yc.nhanVienXuLy.username = :username"
            );
            query.setParameter("username", username);
            return query.getResultList();
        }
    }

}
