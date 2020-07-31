package cn.zensezz.container;


import cn.zensezz.servlet.HttpServlet;
import cn.zensezz.util.AntUtil;

import java.util.HashMap;
import java.util.Map;

public class ServletContainer {

	private static final Map<String, HttpServlet> SERVLET_CONTAINER=new HashMap<String, HttpServlet>();
	
	
	public static HttpServlet getServlet(String path){
		HttpServlet servlet=SERVLET_CONTAINER.get(path);
		if(servlet!=null){
			return servlet;
		}
		for(String patt:SERVLET_CONTAINER.keySet()){
			if(AntUtil.isAntMatch(path, patt)){
				return SERVLET_CONTAINER.get(patt);
			}
		}
		return null;
	}
	
	public static void putServlet(String path,HttpServlet servlet){
		SERVLET_CONTAINER.put(path, servlet);
	}
}
