package cn.zensezz.threadpool;

import cn.zensezz.constant.SimplipfyConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class SimplifyThreadPool {

	public static final ExecutorService  HTTP_POOL =  new ThreadPoolExecutor(100, SimplipfyConstant.HTTP_THREAD_NUM,
	          10,TimeUnit.SECONDS,
	          new LinkedBlockingQueue<Runnable>()); 
	
	public static final ExecutorService  SIMPLIFY_POOL =  new ThreadPoolExecutor(5,SimplipfyConstant.SIMPLIPFY_THREAD_NUM,
	          10,TimeUnit.SECONDS,
	          new LinkedBlockingQueue<Runnable>());
	
}