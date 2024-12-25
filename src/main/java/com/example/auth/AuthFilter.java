package com.example.auth;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        HttpSession session = req.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("username") != null);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        if (path.endsWith("login.jsp") || path.endsWith("login")) {
            chain.doFilter(request, response);
            return;
        }

        if (!loggedIn) {
            res.sendRedirect("login.jsp");
            return;
        }

        if (path.contains("/admin") && !"ADMIN".equals(role)) {
            res.sendRedirect("unauthorized.jsp");
            return;
        }

        if (path.contains("/support") && !"SUPPORT".equals(role)) {
            res.sendRedirect("unauthorized.jsp");
            return;
        }

        if (path.contains("/employee") && !"EMPLOYEE".equals(role)) {
            res.sendRedirect("unauthorized.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}
