package cn.zensezz.exception;

public class PageNotFoundException extends RuntimeException{

    public PageNotFoundException(String msg){
        super(msg);
    }

}
