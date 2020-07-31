package cn.zensezz.builder;

import cn.hutool.core.util.StrUtil;
import cn.zensezz.constant.SimplipfyConstant;
import cn.zensezz.entity.HttpServletRequest;
import cn.zensezz.entity.HttpServletResponse;
import cn.zensezz.exception.BadRequestException;
import cn.zensezz.exception.PageNotFoundException;
import cn.zensezz.exception.ResponseNotInitException;
import cn.zensezz.process.SimplifyProcess;
import cn.zensezz.util.GZipUtils;

import java.io.IOException;
import java.text.MessageFormat;

public abstract class HttpBuilder {
    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected static final String splitFlag = "\r\n\r\n";

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void buildResponse() throws IOException {
        if (response == null) {
            response = new HttpServletResponse();
        }
        buildResponse(response.getHttpCode(), response.getOutputStream().toByteArray());
    }

    public void buildResponse(byte[] data) throws IOException {
        if (response == null) {
            response = new HttpServletResponse();
        }
        buildResponse(response.getHttpCode(), data);
    }

    public void buildResponse(int httpCode, String msg) throws IOException {
        if (response == null) {
            response = new HttpServletResponse();
        }
        buildResponse(httpCode, msg.getBytes(SimplipfyConstant.ENCODE));
    }

    public void buildResponse(int httpCode, byte[] data) throws IOException {
        if (response == null) {
            response = new HttpServletResponse();
        }
        buildResponseHeader();
        if (SimplipfyConstant.OPENGZIP) {
            response.setHeader("Content-Encoding", "gzip");
            data = GZipUtils.compress(data);
        }
        Integer contextLength = 0;
        if (data != null) {
            contextLength = data.length;
        }
        response.setHeader("Content-Length", contextLength.toString());
        StringBuilder responseHeader = new StringBuilder("HTTP/1.1 ").append(httpCode).append(" ").append("\r\n");
        for (String key : response.getHeaders().keySet()) {
            for (String header : response.getHeader(key)) {
                responseHeader.append(key).append(": ").append(header).append("\r\n");
            }
        }
        responseHeader.append("\r\n");
        response.getOutputStream().reset();
        response.getOutputStream().write(responseHeader.toString().getBytes(SimplipfyConstant.ENCODE));
        if (!StrUtil.isBlankIfStr(data)) {
            response.getOutputStream().write(data);
        }
    }

    public void buildResponseHeader() throws IOException {
        if (response == null) {
            throw new ResponseNotInitException("Response尚未初始化");
        }
        response.setHeader("Connection", "close");
        response.setHeader("Server", "simplify_tomcat/1.0 By zensezz");
        if (!response.containsHeader("Content-Type")) {
            response.setHeader("Content-Type", "text/html");
        }
        if (SimplipfyConstant.OPENGZIP) {
            response.setHeader("Content-Encoding", "gzip");
        }
        if (request != null && request.isSessionCread()) {
            String cookie = MessageFormat.format("{0}={1}; HttpOnly", SimplipfyConstant.SESSION_ID_FIELD_NAME,
                    request.getSessionId());
            response.setHeader("Set-Cookie", cookie);
        }
    }

    public abstract void buildRequestHeader();

    private void destroy() {
        if (response != null && response.getOutputStream() != null) {
            try {
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (request != null && request.getInputStream() != null) {
            try {
                request.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void builder() {
        try {
            buildRequest();
            buildRequestHeader();
            this.response = new HttpServletResponse();
            SimplifyProcess.doService(this);
            buildResponse();
        } catch (BadRequestException e) {
            e.printStackTrace();
            try {
                buildResponse(400, "400 bad request");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                buildResponse(500, "error execution");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (PageNotFoundException e) {
            try {
                buildResponse(404, "page not found!");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                buildResponse(500, "error execution");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void flushAndClose() {
        try {
            flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            destroy();
        }
    }

    protected abstract void buildRequest() throws Exception;

    protected abstract void flush() throws IOException;
}
