package cn.zensezz.container;

import cn.zensezz.servlet.HttpFilter;

import java.util.ArrayList;
import java.util.List;

public class FilterContainer {

	public static final List<HttpFilter> FILTER_CONTAINER=new ArrayList<HttpFilter>();
	
	public static void pushFilter(HttpFilter filter){
		FILTER_CONTAINER.add(filter);
	}
}
