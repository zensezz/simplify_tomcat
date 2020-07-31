package cn.zensezz.servlet;

import cn.zensezz.entity.ApplicationFilterChain;
import cn.zensezz.entity.HttpServletRequest;
import cn.zensezz.entity.HttpServletResponse;

import java.io.IOException;

public abstract class HttpFilter {

    private String mapping;

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, ApplicationFilterChain chain) throws IOException;
}