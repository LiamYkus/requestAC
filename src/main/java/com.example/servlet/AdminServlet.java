package com.example.servlet;

import com.example.entity.NhanVien;
import com.example.entity.Role;
import com.example.entity.YeuCau;
import com.example.utils.HibernateUtil;
import com.example.utils.PasswordUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action != null) {
                switch (action) {
                    case "listEmployees":
                        showEmployeeList(request, response);
                        break;
                    case "viewRequests":
                        showRequestList(request, response);
                        break;
                    case "dashboard":
                        showDashboard(request, response);
                        break;
                    default:
                        response.sendRedirect("admin.jsp");
                        break;
                }
            } else {
                response.sendRedirect("admin.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp?error=An error occurred while processing the request.");
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Count employees and support staff
            Long employeeCount = (Long) session.createQuery("SELECT COUNT(*) FROM NhanVien WHERE loaitaikhoan = :role")
                    .setParameter("role", Role.EMPLOYEE)
                    .uniqueResult();

            Long supportCount = (Long) session.createQuery("SELECT COUNT(*) FROM NhanVien WHERE loaitaikhoan = :role")
                    .setParameter("role", Role.SUPPORT)
                    .uniqueResult();

            // Count total requests
            Long requestCount = (Long) session.createQuery("SELECT COUNT(*) FROM YeuCau").uniqueResult();

            // Pass statistics to the JSP
            request.setAttribute("employeeCount", employeeCount);
            request.setAttribute("supportCount", supportCount);
            request.setAttribute("requestCount", requestCount);

            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin.jsp?error=Cannot load dashboard statistics.");
        }
    }


    private void showRequestList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<NhanVien> supportQuery = session.createQuery("FROM NhanVien WHERE loaitaikhoan = :role", NhanVien.class);
            supportQuery.setParameter("role", Role.SUPPORT);
            List<NhanVien> danhSachSupport = supportQuery.getResultList();

            Query<YeuCau> requestQuery = session.createQuery("FROM YeuCau", YeuCau.class);
            List<YeuCau> danhSachYeuCau = requestQuery.getResultList();

            request.setAttribute("danhSachSupport", danhSachSupport);
            request.setAttribute("danhSachYeuCau", danhSachYeuCau);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải danh sách yêu cầu.");
        }
        request.getRequestDispatcher("requestEmployee.jsp").forward(request, response);
    }

    private void assignRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mayeucauStr = request.getParameter("mayeucau");
        String supportUsername = request.getParameter("supportStaff");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Lấy yêu cầu
            YeuCau yeuCau = session.get(YeuCau.class, Integer.parseInt(mayeucauStr));
            // Lấy nhân viên support
            NhanVien supportStaff = session.get(NhanVien.class, supportUsername);

            if (yeuCau != null && supportStaff != null) {
                // Gán nhân viên support vào yêu cầu
                yeuCau.setManvXuly(supportStaff);
                session.update(yeuCau);
                transaction.commit();
                request.setAttribute("successMessage", "Gán yêu cầu thành công!");
            } else {
                request.setAttribute("errorMessage", "Không thể gán yêu cầu.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi gán yêu cầu.");
        }

        response.sendRedirect("admin?action=viewRequests");
    }




    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "add": // Thêm tài khoản
                    addEmployee(request, response);
                    break;
                case "updateStatus": // Cập nhật trạng thái kích hoạt
                    updateEmployeeStatus(request, response);
                    break;
                case "assignRequest": // Gán yêu cầu cho nhân viên support
                    assignRequest(request, response);
                    break;
                default:
                    response.sendRedirect("list-Employee");
                    break;
            }
        } else {
            response.sendRedirect("listEmployee.jsp");
        }
    }

    private void updateEmployeeStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        boolean currentStatus = Boolean.parseBoolean(request.getParameter("currentStatus"));

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Lấy nhân viên từ database
            NhanVien nhanVien = session.get(NhanVien.class, username);
            if (nhanVien != null) {
                // Đảo ngược trạng thái kích hoạt
                nhanVien.setKichhoat(!currentStatus);
                session.update(nhanVien);
                transaction.commit();
            }

            response.sendRedirect("admin?action=listEmployees");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("listEmployee.jsp?error=Cannot update status");
        }
    }


    private void showEmployeeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<NhanVien> danhSachNhanVien = session.createQuery(
                            "FROM NhanVien WHERE loaitaikhoan IN (:roles)", NhanVien.class)
                    .setParameterList("roles", Arrays.asList(Role.EMPLOYEE, Role.SUPPORT))
                    .list();

            request.setAttribute("danhSachNhanVien", danhSachNhanVien);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Không thể lấy danh sách nhân viên.");
        }

        request.getRequestDispatcher("listEmployee.jsp").forward(request, response);
    }

    private void addEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = PasswordUtil.hashPassword(request.getParameter("password")); // Sử dụng mã hóa mật khẩu
        String hoten = request.getParameter("hoten");
        int loaitaikhoan = Integer.parseInt(request.getParameter("loaitaikhoan"));
        String ngaysinhStr = request.getParameter("ngaysinh");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Chỉ cho phép vai trò là Nhân viên hoặc Nhân viên support
            if (loaitaikhoan != 1 && loaitaikhoan != 2) {
                request.setAttribute("errorMessage", "Vai trò không hợp lệ.");
                response.sendRedirect("admin?action=list");
                return;
            }

            // Tạo đối tượng nhân viên
            NhanVien nhanVien = new NhanVien();
            nhanVien.setUsername(username);
            nhanVien.setPassword(password);
            nhanVien.setHoten(hoten);
            nhanVien.setLoaitaikhoan(Role.values()[loaitaikhoan]); // Sử dụng enum Role
            nhanVien.setKichhoat(true);

            if (ngaysinhStr != null && !ngaysinhStr.isEmpty()) {
                nhanVien.setNgaysinh(Date.valueOf(ngaysinhStr));
            }

            session.save(nhanVien);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi thêm nhân viên.");
        }

        // Quay lại danh sách nhân viên
        response.sendRedirect("admin?action=listEmployees");
    }
}
