package vn.iotstar.controllers;

import java.io.IOException;


import vn.iotstar.services.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.models.User;
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet{
	public static final String SESSION_USERNAME = "username";
	public static final String COOKIE_REMEMBER = "username";

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    HttpSession session = req.getSession(false);
	    
	    // Kiểm tra session
	    if (session != null && session.getAttribute("account") != null) {
	        resp.sendRedirect(req.getContextPath() + "/waiting");
	        return;
	    }
	
	    // Kiểm tra cookie
	    Cookie[] cookies = req.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals("username")) {
	                session = req.getSession(true);
	                session.setAttribute("username", cookie.getValue());
	                resp.sendRedirect(req.getContextPath() + "/waiting");
	                return;
	            }
	        }
	    }
	
	    // Nếu không có session và cookie, chuyển hướng đến trang đăng nhập
	    req.getRequestDispatcher("bt_web_t3/login.jsp").forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    resp.setContentType("text/html");
	    resp.setCharacterEncoding("UTF-8");
	    req.setCharacterEncoding("UTF-8");

	    String username = req.getParameter("username");
	    String password = req.getParameter("password");
	    boolean isRememberMe = false;
	    String remember = req.getParameter("remember");

	    if ("on".equals(remember)) {
	        isRememberMe = true;
	    }

	    String alertMsg = "";
	    
	    if (username.isEmpty() || password.isEmpty()) {
	        alertMsg = "Tài khoản hoặc mật khẩu không được rỗng";
	        req.setAttribute("alert", alertMsg);
	        req.getRequestDispatcher("bt_web_t3/login.jsp").forward(req, resp);
	        return;
	    }

	    IUserService service = new UserServiceImpl();
	    User user = service.login(username, password);

	    if (user != null) {
	        HttpSession session = req.getSession(true);
	        session.setAttribute("account", user);
	        if (isRememberMe) {
	            saveRemeberMe(resp, username);
	        }
	        resp.sendRedirect(req.getContextPath() + "/waiting");
	    } else {
	        alertMsg = "Tài khoản hoặc mật khẩu không đúng";
	        req.setAttribute("alert", alertMsg);
	        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
	    }
	}
	
	private void saveRemeberMe(HttpServletResponse response, String username) {
	    Cookie cookie = new Cookie(COOKIE_REMEMBER, username);
	    cookie.setMaxAge(30 * 60); // Cookie tồn tại trong 30 phút
	    response.addCookie(cookie);
	}

}
