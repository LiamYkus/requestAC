package com.example.servlet;

import com.example.entity.DoUuTien;
import com.example.entity.NhanVien;
import com.example.entity.YeuCau;
import com.example.utils.HibernateUtil;
import com.example.utils.PasswordUtil;
import com.example.utils.UserUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/employee")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class EmployeeServlet extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "images";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        NhanVien nhanVien = UserUtils.getLoggedInUser(httpSession);

        if (nhanVien != null) {
            request.setAttribute("nhanVien", nhanVien);
        } else {
            response.sendRedirect("login.jsp");
            return;
        }

        // Forward to the appropriate JSP
        String action = request.getParameter("action");
        if ("clearMessage".equals(action)) {
            request.getSession().removeAttribute("successMessage");
            response.setStatus(HttpServletResponse.SC_OK);
        }
        if ("viewRequests".equals(action)) {
            viewRequests(request,response);
        } else if ("profile".equals(action)) {
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("employee.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        if ("updateProfile".equals(action)) {
            updateProfile(request, response);
        } else if ("changePassword".equals(action)) {
            changePassword(request, response);
        } else if ("addRequest".equals(action)) {
            addRequest(request, response);
        }else {
            response.sendRedirect("employee?action=dashboard");
        }
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String hoten = request.getParameter("hoten");
        String ngaysinhStr = request.getParameter("ngaysinh");

        // Handle file upload
        String uploadedFileName = null;
        for (Part part : request.getParts()) {
            if (part.getName().equals("profilePicture") && part.getSize() > 0) {
                String fileName = extractFileName(part);
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;

                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                String filePath = uploadPath + File.separator + fileName;
                part.write(filePath);

                uploadedFileName = UPLOAD_DIRECTORY + "/" + fileName; // Save relative path for database
            }
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            NhanVien nhanVien = session.get(NhanVien.class, username);
            if (nhanVien != null) {
                nhanVien.setHoten(hoten);
                if (ngaysinhStr != null && !ngaysinhStr.isEmpty()) {
                    nhanVien.setNgaysinh(Date.valueOf(ngaysinhStr));
                }
                if (uploadedFileName != null) {
                    nhanVien.setHinhanh(uploadedFileName); // Save the image path in the database
                }
                session.update(nhanVien);
                transaction.commit();
                request.getSession().setAttribute("successMessage", "Cập nhật thông tin thành công.");
            } else {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy tài khoản.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật thông tin.");
        }

        response.sendRedirect("employee?action=profile");
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String) request.getSession().getAttribute("username");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.getSession().setAttribute("errorMessage", "Mật khẩu mới và xác nhận mật khẩu không khớp.");
            response.sendRedirect("employee");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            NhanVien nhanVien = session.get(NhanVien.class, username);
            if (nhanVien != null && PasswordUtil.verifyPassword(currentPassword, nhanVien.getPassword())) {
                nhanVien.setPassword(PasswordUtil.hashPassword(newPassword));
                session.update(nhanVien);
                transaction.commit();
                request.getSession().setAttribute("successMessage", "Đổi mật khẩu thành công.");
            } else {
                request.getSession().setAttribute("errorMessage", "Mật khẩu hiện tại không chính xác.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Có lỗi xảy ra khi đổi mật khẩu.");
        }

        response.sendRedirect("employee");
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String content : contentDisp.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return null;
    }

    private void viewRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String searchQuery = request.getParameter("searchQuery");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM YeuCau WHERE manvGui.username = :username";

            // Add search condition if the query is present
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                hql += " AND (tieude LIKE :searchQuery OR noidung LIKE :searchQuery)";
            }

            Query<YeuCau> query = session.createQuery(hql, YeuCau.class);
            query.setParameter("username", username);

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                query.setParameter("searchQuery", "%" + searchQuery.trim() + "%");
            }

            List<YeuCau> requests = query.list();
            request.setAttribute("requests", requests);

            // Fetch priority levels
            List<DoUuTien> priorities = session.createQuery("FROM DoUuTien", DoUuTien.class).list();
            request.setAttribute("priorities", priorities);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Không thể tải danh sách yêu cầu.");
        }

        request.getRequestDispatcher("listRequest.jsp").forward(request, response);
    }


    private void addRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String tieude = request.getParameter("tieude");
        String noidung = request.getParameter("noidung");
        int doUuTienId = Integer.parseInt(request.getParameter("doUuTien"));

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Fetch the employee
            NhanVien nhanVien = session.get(NhanVien.class, username);

            // Fetch the priority level
            DoUuTien doUuTien = session.get(DoUuTien.class, doUuTienId);

            // Create the request
            YeuCau yeuCau = new YeuCau();
            yeuCau.setTieude(tieude);
            yeuCau.setNoidung(noidung);
            yeuCau.setNgaygui(new Date(System.currentTimeMillis()));
            yeuCau.setDoUuTien(doUuTien);
            yeuCau.setManvGui(nhanVien);

            session.save(yeuCau);
            transaction.commit();

            request.getSession().setAttribute("successMessage", "Thêm yêu cầu thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Có lỗi xảy ra khi thêm yêu cầu.");
        }

        response.sendRedirect("employee?action=viewRequests");
    }
}
