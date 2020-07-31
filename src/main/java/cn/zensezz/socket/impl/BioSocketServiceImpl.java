package cn.zensezz.socket.impl;

import cn.zensezz.builder.BioHttpBuilder;
import cn.zensezz.builder.HttpBuilder;
import cn.zensezz.socket.SocketService;
import cn.zensezz.threadpool.SimplifyThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BioSocketServiceImpl implements SocketService {

    private ServerSocket server;

    public void openPort(Integer port, Integer timeOut) throws IOException {
        server = new ServerSocket(port);
        server.setSoTimeout(timeOut);
    }

    public void doService() throws IOException {
        while (true) {
            Socket socket = server.accept();
            doSocket(socket);
        }
    }
    private void doSocket(final Socket socket) {
        SimplifyThreadPool.HTTP_POOL.execute(() -> {
            try {
                HttpBuilder builder = new BioHttpBuilder(socket);
                builder.builder();
                builder.flushAndClose();
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
