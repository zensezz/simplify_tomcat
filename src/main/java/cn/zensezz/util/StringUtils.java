package cn.zensezz.util;

import cn.hutool.core.util.StrUtil;

public class StringUtils {

    public static Integer isNull(Object... objs) {
        if (StrUtil.isBlankIfStr(objs)) {
            return 0;
        }
        for (int i = 0; i < objs.length; i++) {
            if (StrUtil.isBlankIfStr(objs[i])) {
                return i;
            }
        }
        return -1;
    }
    public static boolean hasNull(Object... objs) {
        return isNull(objs) > -1;
    }

}
