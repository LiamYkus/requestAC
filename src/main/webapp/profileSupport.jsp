<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Thông tin cá nhân</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="wrapper d-flex align-items-stretch">
    <nav id="sidebar">
        <div class="custom-menu">
            <button type="button" id="sidebarCollapse" class="btn btn-primary">
                <i class="fa fa-bars"></i>
                <span class="sr-only">Toggle Menu</span>
            </button>
        </div>
        <div class="p-4 pt-5">
            <div class="p-4 pt-5 d-flex align-items-center" style="min-height: 70px;">
                <img src="${nhanVien.hinhanh}" alt="Profile Picture"
                     class="img-thumbnail me-3"
                     style="width: 50px; height: 50px; object-fit: cover; border-radius: 50%;">
                <div>
                    <h6 class="m-0">Chào,</h6>
                    <h5 class="m-0">${nhanVien.hoten}</h5>
                </div>
            </div>
            <ul class="list-unstyled components mb-5">
                <li>
                    <a href="support?action=dashboard">Trang chủ</a>
                </li>
                <li class="active">
                    <a href="support?action=viewAssignedRequests">Yêu cầu được giao</a>
                </li>
                <li>
                    <a href="support?action=profile">Thông tin cá nhân</a>
                </li>
                <li>
                    <a href="logout">Đăng xuất</a>
                </li>
            </ul>
        </div>
    </nav>

    <!-- Page Content -->
    <div id="content" class="p-4 p-md-5 pt-5">
        <h2 class="mb-4">Thông tin cá nhân</h2>

        <!-- Success/Error Messages -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <form method="post" action="support" enctype="multipart/form-data">
            <input type="hidden" name="action" value="updateProfile">
            <div class="mb-3">
                <label for="username" class="form-label">Tên đăng nhập</label>
                <input type="text" class="form-control" id="username" name="username" value="${nhanVien.username}" readonly>
            </div>
            <div class="mb-3">
                <label for="hoten" class="form-label">Họ tên</label>
                <input type="text" class="form-control" id="hoten" name="hoten" value="${nhanVien.hoten}" required>
            </div>
            <div class="mb-3">
                <label for="profilePicture" class="form-label">Ảnh đại diện</label>
                <input type="file" class="form-control" id="profilePicture" name="profilePicture">
                <img src="${nhanVien.hinhanh}" alt="Profile Picture" class="img-thumbnail mt-2" style="width: 150px;">
            </div>
            <div class="mb-3">
                <label for="ngaysinh" class="form-label">Ngày sinh</label>
                <input type="date" class="form-control" id="ngaysinh" name="ngaysinh" value="${nhanVien.ngaysinh}">
            </div>
            <button type="submit" class="btn btn-primary">Cập nhật</button>
            <button type="button" class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                Đổi mật khẩu
            </button>
        </form>
        <!-- Modal for Changing Password -->
        <div class="modal fade" id="changePasswordModal" tabindex="-1" aria-labelledby="changePasswordModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form method="post" action="support">
                        <input type="hidden" name="action" value="changePassword">
                        <div class="modal-header">
                            <h5 class="modal-title" id="changePasswordModalLabel">Đổi mật khẩu</h5>
                            <button type="button" class="btn-close border" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label for="currentPassword" class="form-label">Mật khẩu hiện tại</label>
                                <input type="password" class="form-control border" id="currentPassword" name="currentPassword" required>
                            </div>
                            <div class="mb-3">
                                <label for="newPassword" class="form-label">Mật khẩu mới</label>
                                <input type="password" class="form-control border" id="newPassword" name="newPassword" required>
                            </div>
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">Xác nhận mật khẩu mới</label>
                                <input type="password" class="form-control border" id="confirmPassword" name="confirmPassword" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            <button type="submit" class="btn btn-primary">Đổi mật khẩu</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

<script src="js/jquery.min.js"></script>
<script src="js/popper.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
