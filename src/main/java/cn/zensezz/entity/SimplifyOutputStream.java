package cn.zensezz.entity;

import cn.zensezz.constant.SimplipfyConstant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SimplifyOutputStream extends ByteArrayOutputStream {

    public void print(String s) throws IOException {
        write(s.getBytes(SimplipfyConstant.ENCODE));
    }

    public void write(String s) throws IOException {
        print(s);
    }

    public void print(boolean b) throws IOException {
        String msg;
        if (b)
            msg = "true";
        else {
            msg = "false";
        }
        print(msg);
    }

    public void print(char c) throws IOException {
        print(String.valueOf(c));
    }

    public void print(int i) throws IOException {
        print(String.valueOf(i));
    }

    public void print(long l) throws IOException {
        print(String.valueOf(l));
    }

    public void print(float f) throws IOException {
        print(String.valueOf(f));
    }

    public void print(double d) throws IOException {
        print(String.valueOf(d));
    }

    public void println() throws IOException {
        print("\r\n");
    }

}