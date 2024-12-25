<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Yêu cầu nhân viên</title>
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
                <li>
                    <a href="admin?action=listEmployees">Danh sách nhân viên</a>
                </li>
                <li class="active">
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
                    Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved | This template is made with <i class="icon-heart" aria-hidden="true"></i> by Liam
                    <!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. --></p>
            </div>

        </div>
    </nav>

    <!-- Page Content  -->
    <div id="content" class="p-4 p-md-5 pt-5">
        <h2 class="mb-4">Danh sách yêu cầu của nhân viên</h2>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>#</th>
                <th>Tiêu đề</th>
                <th>Nội dung</th>
                <th>Ngày gửi</th>
                <th>Độ ưu tiên</th>
                <th>Người xử lý</th>
                <th>Gán cho</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="yeuCau" items="${danhSachYeuCau}">
                <tr>
                    <td>${yeuCau.mayeucau}</td>
                    <td>${yeuCau.tieude}</td>
                    <td>${yeuCau.noidung}</td>
                    <td>${yeuCau.ngaygui}</td>
                    <td>${yeuCau.doUuTien.tendouutien}</td>
                    <td>
                        <c:choose>
                            <c:when test="${yeuCau.nhanVienXuLy != null}">
                                ${yeuCau.nhanVienXuLy.hoten}
                            </c:when>
                            <c:otherwise>
                                Chưa xử lý
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${yeuCau.nhanVienXuLy == null}">
                            <!-- Dropdown để chọn nhân viên support -->
                            <form method="post" action="admin" onsubmit="return confirmAssign(this);">
                                <input type="hidden" name="action" value="assignRequest">
                                <input type="hidden" name="mayeucau" value="${yeuCau.mayeucau}">
                                <select class="form-select" name="supportStaff">
                                    <c:forEach var="support" items="${danhSachSupport}">
                                        <option value="${support.username}">
                                                ${support.hoten}
                                        </option>
                                    </c:forEach>
                                </select>
                                <button type="submit" class="btn btn-primary mt-2">Gán</button>
                            </form>
                        </c:if>
                        <c:if test="${yeuCau.nhanVienXuLy != null}">
                            ${yeuCau.nhanVienXuLy.hoten}
                        </c:if>
                    </td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</div>
<script>
    function confirmAssign(form) {
        const selectedStaff = form.supportStaff.options[form.supportStaff.selectedIndex].text;
        return confirm(`Bạn có chắc chắn muốn gán yêu cầu cho nhân viên này?`);
    }
</script>

<script src="js/jquery.min.js"></script>
<script src="js/popper.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
