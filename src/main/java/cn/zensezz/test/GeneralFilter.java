package cn.zensezz.test;

import cn.zensezz.annotation.Filter;
import cn.zensezz.entity.ApplicationFilterChain;
import cn.zensezz.entity.HttpServletRequest;
import cn.zensezz.entity.HttpServletResponse;
import cn.zensezz.servlet.HttpFilter;

import java.io.IOException;

@Filter("/")
public class GeneralFilter extends HttpFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, ApplicationFilterChain chain) throws IOException {
		chain.doFilter(request, response);
	}

}
