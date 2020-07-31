package cn.zensezz.servlet;

import cn.zensezz.entity.HttpServletRequest;
import cn.zensezz.entity.HttpServletResponse;

import java.io.IOException;

public abstract class HttpServlet{
	
	public abstract void doService(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
