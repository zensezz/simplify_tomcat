package cn.zensezz.process;

import cn.zensezz.builder.HttpBuilder;
import cn.zensezz.container.ServletContainer;
import cn.zensezz.entity.ApplicationFilterChain;
import cn.zensezz.servlet.HttpServlet;
import cn.zensezz.threadpool.SimplifyThreadPool;

public class SimplifyProcess {

	public static void doService(HttpBuilder build) throws 	Exception {
			HttpServlet servlet = ServletContainer.getServlet(build.getRequest().getRequestURI());
			ApplicationFilterChain chain=new ApplicationFilterChain(servlet);
			chain.doFilter(build.getRequest(), build.getResponse());
	}

	static {
		SimplifyThreadPool.SIMPLIFY_POOL.execute(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}
}