package cn.zensezz.socket.impl;

import cn.zensezz.socket.SocketService;

import java.io.IOException;
import java.net.ServerSocket;

public class NioSocketServiceImpl implements SocketService {

    private ServerSocket server;

    public void openPort(Integer port, Integer timeOut) throws IOException {
        server = new ServerSocket(port);
        server.setSoTimeout(timeOut);
    }

    public void doService() throws IOException {

    }
}
