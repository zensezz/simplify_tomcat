package cn.zensezz.test;

import cn.zensezz.annotation.Servlet;
import cn.zensezz.entity.HttpServletRequest;
import cn.zensezz.entity.HttpServletResponse;
import cn.zensezz.servlet.HttpServlet;

import java.io.IOException;

@Servlet("/test.do")
public class SimplifyTestServlet extends HttpServlet {


	@Override
	public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getOutputStream().write("hello");
	}

}
