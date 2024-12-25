<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Danh sách yêu cầu được giao</title>
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
        <h2 class="mb-4">Dashboard</h2>
        <div class="row">
            <div class="col-md-4">
                <div class="card text-white bg-primary mb-3">
                    <div class="card-header">Tổng số nhân viên</div>
                    <div class="card-body">
                        <h5 class="card-title">${totalEmployees}</h5>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-white bg-success mb-3">
                    <div class="card-header">Nhân viên hỗ trợ</div>
                    <div class="card-body">
                        <h5 class="card-title">${totalSupportEmployees}</h5>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card text-white bg-info mb-3">
                    <div class="card-header">Tổng số yêu cầu</div>
                    <div class="card-body">
                        <h5 class="card-title">${totalRequests}</h5>
                    </div>
                </div>
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
