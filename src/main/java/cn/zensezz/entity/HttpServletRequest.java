package cn.zensezz.entity;

import cn.hutool.core.util.StrUtil;
import cn.zensezz.aop.ParamentAop;
import cn.zensezz.constant.SimplipfyConstant;
import cn.zensezz.util.ByteUtils;
import cn.zensezz.util.GZipUtils;
import lombok.Data;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class HttpServletRequest {

    private String method = "GET";

    private String protocol;

    private String requestURI;

    private String requestURL;

    private String relativeURI;

    private Map<String, String> header;

    private InputStream inputStream;

    private String sessionId;

    private boolean isGzip = false;

    private boolean isSessionCread = false;

    private String scheme = (SimplipfyConstant.HTTP_PORT == 443 ? "https" : "http");

    private String basePath;

    private Map<String, List<Object>> reqParams;

    private String queryString = "";

    private Integer contextLength = 0;

    public MultipartFile getFile(String paramName) {
        if (reqParams == null) {
            initParams();
        }
        List<Object> paramValues = reqParams.get(paramName);
        if (StrUtil.isBlankIfStr(paramValues)) {
            return null;
        }
        Object value = paramValues.get(0);
        if (!MultipartFile.class.isAssignableFrom(value.getClass())) {
            return null;
        }
        return (MultipartFile) value;
    }
    public String getParament(String paramName) {
        if (reqParams == null) {
            initParams();
        }
        List<Object> paramValues = reqParams.get(paramName);
        if (StrUtil.isBlankIfStr(paramValues)) {
            return null;
        }
        Object value=paramValues.get(0);
        if(MultipartFile.class.isAssignableFrom(value.getClass())){
            try {
                return new String(((MultipartFile)value).getFileContext(),SimplipfyConstant.ENCODE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return value.toString();
    }

    private void initParams() {
        reqParams = ParamentAop.buildGeneralParams(queryString);
        if (header.containsKey("Content-Type") && header.get("Content-Type").contains("multipart/form-data")) {
            String line = header.get("Content-Type");
            String[] dabbles = line.split(";");
            String boundary = "";
            for (String dabble : dabbles) {
                int index = dabble.indexOf("=");
                if (index < 1 || index > dabble.length()) {
                    continue;
                }
                String name = dabble.substring(0, dabble.indexOf("=")).trim();
                String value = dabble.substring(dabble.indexOf("=") + 1);
                if (name.equals("boundary")) {
                    boundary = value;
                }
            }
            byte[] data = ByteUtils.getBytes(inputStream, contextLength);
            Map<String, List<Object>> paramMap = ParamentAop.buildMultipartParams(data, boundary);
            reqParams = mergeParaMap(reqParams, paramMap);
        } else {
            String postContext = getPostContext();
            if (!StrUtil.isBlankIfStr(postContext)) {
                Map<String, List<Object>> paramMap = ParamentAop.buildGeneralParams(postContext);
                reqParams = mergeParaMap(reqParams, paramMap);
            }
        }
    }

    public String getPostContext() {
        try {
            byte[] data = ByteUtils.getBytes(inputStream,contextLength);
            if (data == null) {
                return null;
            }
            if (isGzip) {
                data = GZipUtils.decompress(data);
            }
            return new String(data, SimplipfyConstant.ENCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private Map<String, List<Object>> mergeParaMap(Map<String, List<Object>> paraMap1,
                                                   Map<String, List<Object>> paraMap2) {

        if (StrUtil.isBlankIfStr(paraMap1)) {
            return paraMap2;
        }
        if (StrUtil.isBlankIfStr(paraMap2)) {
            return paraMap1;
        }
        for (String key : paraMap1.keySet()) {
            if (!paraMap2.containsKey(key)) {
                paraMap2.put(key, paraMap1.get(key));
                continue;
            }
            paraMap2.get(key).addAll(paraMap1.get(key));
        }
        return paraMap2;
    }

    public void setHeader(String name,String value) {
        if(header==null){
            header=new HashMap<String, String>();
        }
        header.put(name, value);
    }

}
