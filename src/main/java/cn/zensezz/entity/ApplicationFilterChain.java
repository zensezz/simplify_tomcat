package cn.zensezz.entity;

import cn.zensezz.container.FilterContainer;
import cn.zensezz.exception.PageNotFoundException;
import cn.zensezz.servlet.HttpFilter;
import cn.zensezz.servlet.HttpServlet;
import cn.zensezz.util.AntUtil;

import java.io.IOException;

public final class ApplicationFilterChain {

	public ApplicationFilterChain(HttpServlet servlet) {
		this.servlet = servlet;
	}

	private int pos = 0;
	private HttpServlet servlet;

	public void doFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (pos < FilterContainer.FILTER_CONTAINER.size()) {
			HttpFilter filter = FilterContainer.FILTER_CONTAINER.get(pos++);
			if (!AntUtil.isAntMatch(request.getRequestURI(), filter.getMapping())) {
				doFilter(request, response);
				return;
			}
			filter.doFilter(request, response, this);
			return;
		}
		if (servlet == null) {
			throw new PageNotFoundException("该页面未找到>>" + request.getRequestURI());
		}
		servlet.doService(request, response);
	}
}
