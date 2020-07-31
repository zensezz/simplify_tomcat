package cn.zensezz.socket;

import java.io.IOException;

public interface SocketService {

     void openPort(Integer port,Integer timeOut) throws IOException;

     void doService() throws IOException;
}
