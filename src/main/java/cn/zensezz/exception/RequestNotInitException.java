package cn.zensezz.exception;

public class RequestNotInitException extends RuntimeException {

    public RequestNotInitException(String msg) {
        super(msg);
    }
}
