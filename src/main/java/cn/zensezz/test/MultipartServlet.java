package cn.zensezz.test;


import cn.hutool.core.util.StrUtil;
import cn.zensezz.annotation.Servlet;
import cn.zensezz.entity.HttpServletRequest;
import cn.zensezz.entity.HttpServletResponse;
import cn.zensezz.servlet.HttpServlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Servlet("/upload.do")
public class MultipartServlet extends HttpServlet {

	@Override
	public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map<String, List<Object>> map = request.getReqParams();
		if (StrUtil.isBlankIfStr(map)) {
			response.getOutputStream().write("未传递任何参数");
			return;
		}
		for (String key : map.keySet()) {
			List<Object> paramValues = map.get(key);
			if (StrUtil.isBlankIfStr(paramValues)) {
				response.getOutputStream().write(key + ">>null");
				response.getOutputStream().write("<br>");
				continue;
			}
			for (Object obj : paramValues) {
				response.getOutputStream().write(key + ">>" + obj.toString());
				response.getOutputStream().write("<br>");
				continue;
			}
		}
	}

}
