package cn.zensezz;

import cn.hutool.core.util.StrUtil;
import cn.zensezz.annotation.Filter;
import cn.zensezz.annotation.Servlet;
import cn.zensezz.constant.SimplipfyConstant;
import cn.zensezz.container.FilterContainer;
import cn.zensezz.container.ServletContainer;
import cn.zensezz.servlet.HttpFilter;
import cn.zensezz.servlet.HttpServlet;
import cn.zensezz.socket.SocketService;
import cn.zensezz.socket.impl.BioSocketServiceImpl;
import cn.zensezz.socket.impl.NioSocketServiceImpl;
import cn.zensezz.test.TestServlet;

public class Main {

    public static void init(Class<?>... clazzs) {
        long startTime = System.currentTimeMillis();
        SocketService socketService ;
        if (SimplipfyConstant.MODEL == 2) {
            socketService = new NioSocketServiceImpl();
        }else {
            socketService = new BioSocketServiceImpl();
        }
        System.out.println("引用模式： " + socketService.getClass().getName());
        try {
            if (StrUtil.isBlankIfStr(clazzs)) {
                System.err.println("初始化Servlet为空");
                return;
            }
            socketService.openPort(SimplipfyConstant.HTTP_PORT, SimplipfyConstant.SESSION_TIMEOUT);
            System.out.println("监听端口： " + SimplipfyConstant.HTTP_PORT);
            for (Class<?> clazz : clazzs) {

                /*if (!SimplifyHttpPart.class.isAssignableFrom(clazz)) {
                    continue;
                }*/
                Servlet servletFlag = clazz.getAnnotation(Servlet.class);
                if (servletFlag != null && !StrUtil.isBlankIfStr(servletFlag.value())) {
                    HttpServlet servlet = (HttpServlet) clazz.getDeclaredConstructor().newInstance();
                    System.out.println("注册Servlet: " + clazz.getName() + "--" + servletFlag.value());
                    ServletContainer.putServlet(servletFlag.value(), servlet);
                }
                Filter filterFlag=clazz.getAnnotation(Filter.class);
                if (filterFlag != null && !StrUtil.isBlankIfStr(filterFlag.value())) {
                    HttpFilter filter = (HttpFilter) clazz.getDeclaredConstructor().newInstance();
                    filter.setMapping(filterFlag.value());
                    System.out.println("注册Servlet: " + clazz.getName() + "--" + filterFlag.value());
                    FilterContainer.pushFilter(filter);
                }
            }
            System.out.println("simplify_tomcat启动完成,耗时: " + (System.currentTimeMillis() - startTime) + "ms");
            // 处理请求
            socketService.doService();
        }catch (Exception e){
            System.err.println("启动simplify_tomcat失败");
        }
    }

    public static void main(String[] args) {
        init(TestServlet.class);
    }

}
