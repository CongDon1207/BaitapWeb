package vn.iotstar.controls;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet(urlPatterns = {"/home"})
public class HelloWorld extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		String ten = req.getParameter("ten");
		String hoLot = req.getParameter("holot");
		PrintWriter out = resp.getWriter();
		out.print("Xin chào: " + hoLot + " " + ten);
	}
}
