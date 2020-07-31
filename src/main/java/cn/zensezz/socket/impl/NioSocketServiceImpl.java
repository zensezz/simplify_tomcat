package cn.zensezz.socket.impl;

import cn.zensezz.builder.HttpBuilder;
import cn.zensezz.builder.NioHttpBuilder;
import cn.zensezz.socket.SocketService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;

public class NioSocketServiceImpl implements SocketService {

    private Selector selector;

    public void openPort(Integer port, Integer timeOut) throws IOException {
        selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(port));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        server.socket().setSoTimeout(timeOut);
    }

    @Override
    public void doService() throws IOException {
        while (true) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                process(key);
            }
        }
    }
    private void process(SelectionKey key) {
        try {
            // 接收请求
            if (key.isAcceptable()) {
                acceptable(key);
                return;
            }
            // 读信息
            if (key.isReadable()) {
                readable(key);
                return;
            }
            // 写事件
            if (key.isWritable()) {
                writable(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                key.channel().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void acceptable(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel channel = server.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);

    }

    private void readable(SelectionKey key) throws ClosedChannelException {
        SocketChannel channel = (SocketChannel) key.channel();
        HttpBuilder builder = new NioHttpBuilder(channel);
        builder.builder();
        SelectionKey sKey = channel.register(selector, SelectionKey.OP_WRITE);
        sKey.attach(builder);
    }

    private void writable(SelectionKey key)  {
        try (SocketChannel ignored = (SocketChannel) key.channel()) {
            HttpBuilder builder = (HttpBuilder) key.attachment();
            builder.flushAndClose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
