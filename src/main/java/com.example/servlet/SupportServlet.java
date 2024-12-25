package com.example.servlet;

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

@WebServlet("/support")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class SupportServlet extends HttpServlet {
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


        String action = request.getParameter("action");

        if (action == null || action.equals("viewAssignedRequests")) {
            viewAssignedRequests(request, response);
        } else if (action.equals("profile")) {
            viewProfile(request, response);
        } else if (action.equals("searchRequests")) {
            searchRequests(request, response);
        } else if (action.equals("dashboard")) {
            request.getRequestDispatcher("support.jsp").forward(request, response);
        } else {
            response.sendRedirect("support?action=viewAssignedRequests");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("updateProfile".equals(action)) {
            updateProfile(request, response);
        }  else if ("changePassword".equals(action)) {
        changePassword(request, response);
        }else {
            response.sendRedirect("support?action=viewAssignedRequests");
        }
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
            response.sendRedirect("support?action=profile");
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

        response.sendRedirect("support?action=profile");
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

        response.sendRedirect("support?action=profile");
    }

    private void viewAssignedRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<YeuCau> query = session.createQuery("FROM YeuCau WHERE nhanVienXuLy.username = :username", YeuCau.class);
            query.setParameter("username", username);
            List<YeuCau> assignedRequests = query.getResultList();

            request.setAttribute("assignedRequests", assignedRequests);
            request.getRequestDispatcher("supportRequests.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("support?action=viewAssignedRequests&error=Error loading requests");
        }
    }

    private void viewProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NhanVien nhanVien = session.get(NhanVien.class, username);
            request.setAttribute("nhanVien", nhanVien);
            request.getRequestDispatcher("profileSupport.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("support?action=viewAssignedRequests&error=Error loading profile");
        }
    }

    private void searchRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String priority = request.getParameter("priority");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder queryStr = new StringBuilder("FROM YeuCau WHERE nhanVienXuLy.username = :username");

            if (startDateStr != null && !startDateStr.isEmpty()) {
                queryStr.append(" AND ngaygui >= :startDate");
            }

            if (endDateStr != null && !endDateStr.isEmpty()) {
                queryStr.append(" AND ngaygui <= :endDate");
            }

            if (priority != null && !priority.isEmpty()) {
                queryStr.append(" AND doUuTien.madouutien = :priority");
            }

            Query<YeuCau> query = session.createQuery(queryStr.toString(), YeuCau.class);
            query.setParameter("username", username);

            if (startDateStr != null && !startDateStr.isEmpty()) {
                query.setParameter("startDate", java.sql.Date.valueOf(startDateStr));
            }

            if (endDateStr != null && !endDateStr.isEmpty()) {
                query.setParameter("endDate", java.sql.Date.valueOf(endDateStr));
            }

            if (priority != null && !priority.isEmpty()) {
                query.setParameter("priority", Integer.parseInt(priority));
            }

            List<YeuCau> assignedRequests = query.getResultList();
            request.setAttribute("assignedRequests", assignedRequests);
            request.getRequestDispatcher("supportRequests.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("support?action=viewAssignedRequests&error=Error searching requests");
        }
    }

}
