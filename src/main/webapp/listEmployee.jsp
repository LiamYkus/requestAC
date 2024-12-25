<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.example.entity.NhanVien" %>
<%@ page import="com.example.entity.Role" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Danh sách nhân viên</title>
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
      <h1><a href="index.html" class="logo">Admin</a></h1>
      <ul class="list-unstyled components mb-5">
        <li>
          <a href="admin?action=dashboard" >Trang chủ</a>
        </li>
        <li class="active">
          <a href="admin?action=listEmployees">Danh sách nhân viên</a>
        </li>
        <li>
          <a href="admin?action=viewRequests">Yêu cầu nhân viên</a>
        </li>
        <li>
          <a href="logout">Đăng xuất</a>
        </li>
      </ul>

      <div class="mb-5">
        <h3 class="h6">Subscribe for newsletter</h3>
        <form action="#" class="colorlib-subscribe-form">
          <div class="form-group d-flex">
            <div class="icon"><span class="icon-paper-plane"></span></div>
            <input type="text" class="form-control" placeholder="Enter Email Address">
          </div>
        </form>
      </div>

      <div class="footer">
        <p><!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
          Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved | This template is made with <i class="icon-heart" aria-hidden="true"></i> by <a href="https://colorlib.com" target="_blank">Colorlib.com</a>
          <!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. --></p>
      </div>

    </div>
  </nav>

  <!-- Page Content  -->
  <div id="content" class="p-4 p-md-5 pt-5">
    <h2 class="mb-4">Danh sách nhân viên</h2>
    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addEmployeeModal">
      Thêm Nhân Viên
    </button>

    <!-- Modal Thêm Nhân Viên -->
    <div class="modal fade" id="addEmployeeModal" tabindex="-1" aria-labelledby="addEmployeeModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <form method="post" action="list-Employee">
            <input type="hidden" name="action" value="add">
            <div class="modal-header">
              <h5 class="modal-title" id="addEmployeeModalLabel">Thêm Nhân Viên</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <!-- Tên đăng nhập -->
              <div class="mb-3">
                <label for="username" class="form-label">Tên đăng nhập</label>
                <input type="text" class="form-control border" id="username" name="username" required>
              </div>

              <!-- Mật khẩu -->
              <div class="mb-3">
                <label for="password" class="form-label">Mật khẩu</label>
                <input type="password" class="form-control border" id="password" name="password" required>
              </div>

              <!-- Họ tên -->
              <div class="mb-3">
                <label for="hoten" class="form-label">Họ tên</label>
                <input type="text" class="form-control border" id="hoten" name="hoten" required>
              </div>

              <!-- Vai trò -->
              <div class="mb-3">
                <label for="loaitaikhoan" class="form-label">Vai trò</label>
                <select class="form-select" id="loaitaikhoan" name="loaitaikhoan" required>
                  <option value="1">Nhân viên support</option>
                  <option value="2">Nhân viên</option>
                </select>
              </div>

              <!-- Ngày sinh -->
              <div class="mb-3">
                <label for="ngaysinh" class="form-label">Ngày sinh</label>
                <input type="date" class="form-control border" id="ngaysinh" name="ngaysinh">
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
              <button type="submit" class="btn btn-primary">Thêm</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Hiển thị thông báo lỗi nếu có -->
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-danger" role="alert">
          ${errorMessage}
      </div>
    </c:if>

    <!-- Bảng danh sách nhân viên -->
    <table class="table table-bordered table-striped">
      <thead>
      <tr>
        <th>#</th>
        <th>Tên đăng nhập</th>
        <th>Họ tên</th>
        <th>Vai trò</th>
        <th>Ngày sinh</th>
        <th>Kích hoạt</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="nhanVien" items="${danhSachNhanVien}" varStatus="status">
        <tr>
          <td>${status.index + 1}</td>
          <td>${nhanVien.username}</td>
          <td>${nhanVien.hoten}</td>
          <td>
            <c:choose>
              <c:when test="${nhanVien.loaitaikhoan == Role.SUPPORT}">Nhân viên support</c:when>
              <c:when test="${nhanVien.loaitaikhoan == Role.EMPLOYEE}">Nhân viên</c:when>
            </c:choose>
          </td>
          <td>${nhanVien.ngaysinh}</td>
          <td>
            <form method="post" action="list-Employee">
              <input type="hidden" name="action" value="updateStatus">
              <input type="hidden" name="username" value="${nhanVien.username}">
              <input type="hidden" name="currentStatus" value="${nhanVien.kichhoat}">
              <button type="submit" class="btn btn-${nhanVien.kichhoat ? 'danger' : 'success'}">
                  ${nhanVien.kichhoat ? 'Hủy kích hoạt' : 'Kích hoạt'}
              </button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>

</div>

<script src="js/jquery.min.js"></script>
<script src="js/popper.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
