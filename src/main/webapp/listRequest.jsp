<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Danh sách yêu cầu của tôi</title>
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
        <<div class="p-4 pt-5">
        <div class="p-4 pt-5 d-flex align-items-center" style="min-height: 70px;">
            <!-- Display Profile Picture -->
            <img src="${nhanVien.hinhanh}" alt="Profile Picture"
                 class="img-thumbnail me-3"
                 style="width: 50px; height: 50px; object-fit: cover; border-radius: 50%;">
            <!-- Display Welcome Message -->
            <div>
                <h6 class="m-0">Chào,</h6>
                <h5 class="m-0">${nhanVien.hoten}</h5>
            </div>
        </div>
        <ul class="list-unstyled components mb-5">
            <li>
                <a href="employee?action=dashboard">Trang chủ</a>
            </li>
            <li class="active">
                <a href="employee?action=viewRequests">Yêu cầu của tôi</a>
            </li>
            <li>
                <a href="employee?action=profile">Thông tin cá nhân</a>
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
        <h2 class="mb-4">Danh sách yêu cầu của tôi</h2>
        <!-- Toast Notification -->
        <div class="toast-container position-fixed top-0 end-0 p-3" style="z-index: 1050;">
            <div class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true" id="successToast">
                <div class="d-flex">
                    <div class="toast-body">
                        Thêm yêu cầu thành công!
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        </div>

        <!-- Modal for Adding Requests -->
        <div class="modal fade" id="addRequestModal" tabindex="-1" aria-labelledby="addRequestModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form method="post" action="employee" accept-charset="UTF-8">
                        <input type="hidden" name="action" value="addRequest">
                        <div class="modal-header">
                            <h5 class="modal-title" id="addRequestModalLabel">Thêm Yêu Cầu</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <!-- Request Title -->
                            <div class="mb-3">
                                <label for="tieude" class="form-label">Tiêu đề</label>
                                <input type="text" class="form-control border" id="tieude" name="tieude" required>
                            </div>

                            <!-- Request Content -->
                            <div class="mb-3">
                                <label for="noidung" class="form-label">Nội dung</label>
                                <textarea class="form-control border" id="noidung" name="noidung" rows="3"></textarea>
                            </div>

                            <!-- Priority Level -->
                            <div class="mb-3">
                                <label for="doUuTien" class="form-label">Độ ưu tiên</label>
                                <select class="form-select" id="doUuTien" name="doUuTien" required>
                                    <c:forEach var="priority" items="${priorities}">
                                        <option value="${priority.madouutien}">${priority.tendouutien}</option>
                                    </c:forEach>
                                </select>
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
        <div class="d-flex justify-content-between align-items-center mb-4">
            <!-- Add Request Button -->
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addRequestModal">
                Thêm Yêu Cầu
            </button>

            <!-- Search Form -->
            <form method="get" action="employee" class="d-flex ms-auto">
                <input type="hidden" name="action" value="viewRequests">
                <input type="text" class="form-control me-2" name="searchQuery" placeholder="Tìm kiếm yêu cầu..." value="${param.searchQuery}">
                <button type="submit" class="btn btn-primary">
                    <i class="fa fa-search"></i>
                </button>
            </form>
        </div>

        <!-- Display Requests Table -->
        <table class="table table-bordered table-striped">
            <thead>
            <tr>
                <th>#</th>
                <th>Tiêu đề</th>
                <th>Nội dung</th>
                <th>Độ ưu tiên</th>
                <th>Ngày gửi</th>
                <th>Người xử lý</th>
                <th>Trạng thái</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="request" items="${requests}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${request.tieude}</td>
                    <td>${request.noidung}</td>
                    <td>${request.doUuTien.tendouutien}</td>
                    <td>${request.ngaygui}</td>
                    <td>
                        <c:if test="${request.manvXuly != null}">
                            ${request.manvXuly.hoten}
                        </c:if>
                        <c:if test="${request.manvXuly == null}">
                            Chưa được xử lý
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${request.manvXuly == null}">
                            <span class="badge bg-warning">Chưa xử lý</span>
                        </c:if>
                        <c:if test="${request.manvXuly != null}">
                            <span class="badge bg-success">Đã xử lý</span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>


</div>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const successMessage = "${successMessage}";
        if (successMessage) {
            const toastElement = document.getElementById("successToast");
            const toast = new bootstrap.Toast(toastElement, { delay: 5000 });
            toast.show();
            fetch('employee?action=clearMessage');
        }
    });
</script>
<script src="js/jquery.min.js"></script>
<script src="js/popper.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
