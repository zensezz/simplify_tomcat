package cn.zensezz.constant;

public class SimplipfyConstant {

    /**
     * Session超时时间
     */
    public static Integer SESSION_TIMEOUT = 60 * 1000 * 10;

    /**
     * Simplipfy HTTP线程数量
     */
    public static Integer HTTP_THREAD_NUM = 500;

    /**
     * Simplipfy 内务线程数量
     */
    public static Integer SIMPLIPFY_THREAD_NUM = 20;

    /**
     * Simplipfy端口
     */
    public static Integer HTTP_PORT=8080;

    /**
     * HttpSocket超时时间
     */
    public static Integer HTTP_SO_TIMEOUT=3000;

    /**
     * HTTP SessionId字段名
     */
    public static String SESSION_ID_FIELD_NAME="COODYSESSID";

    /**
     * 全局编码
     */
    public static String ENCODE="UTF-8";

    /**
     * 打开Gzip
     */
    public static boolean OPENGZIP=true;

    /**
     * 模式 1Bio  2Nio
     */
    public static Integer MODEL=1;

    /**
     * 最大Head长度
     */
    public static Integer MAX_HEADER_LENGTH=8192;

}
