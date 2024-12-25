package com.example.servlet;

import com.example.entity.NhanVien;
import com.example.utils.HibernateUtil;
import com.example.utils.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user-login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Truy vấn lấy thông tin người dùng theo username và trạng thái kích hoạt
            Query<NhanVien> query = session.createQuery(
                    "FROM NhanVien WHERE username = :username AND kichhoat = true", NhanVien.class);
            query.setParameter("username", username);

            NhanVien user = query.uniqueResult();

            if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
                // Nếu mật khẩu đúng và tài khoản đã kích hoạt, tạo session và điều hướng
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("username", user.getUsername());
                httpSession.setAttribute("role", user.getLoaitaikhoan().name());

                switch (user.getLoaitaikhoan()) {
                    case ADMIN:
                        response.sendRedirect("admin?action=dashboard");
                        break;
                    case SUPPORT:
                        response.sendRedirect("support?action=dashboard");
                        break;
                    case EMPLOYEE:
                        response.sendRedirect("employee?action=dashboard");
                        break;
                }
            } else {
                // Thông báo lỗi nếu tài khoản không tồn tại, bị khóa, hoặc thông tin không hợp lệ
                request.setAttribute("errorMessage", "Invalid username, password, or account is not activated!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
