package cn.zensezz.builder;

import cn.hutool.core.util.StrUtil;
import cn.zensezz.constant.SimplipfyConstant;
import cn.zensezz.entity.HttpServletRequest;
import cn.zensezz.exception.BadRequestException;
import cn.zensezz.exception.NotConnectionException;
import cn.zensezz.exception.RequestNotInitException;
import cn.zensezz.exception.SimplifyException;
import cn.zensezz.util.ByteUtils;
import cn.zensezz.util.StringUtils;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

@Data
public class BioHttpBuilder extends HttpBuilder {

    private Socket socket;

    public BioHttpBuilder(Socket socket) {
        if (socket == null) {
            throw new NotConnectionException("未实例化Socket");
        }
        this.socket = socket;
    }

    public void buildRequestHeader() {
        if (request == null) {
            throw new RequestNotInitException("Request尚未初始化");
        }
        try {
            byte[] data = ByteUtils.getBytes(socket.getInputStream(), SimplipfyConstant.MAX_HEADER_LENGTH);
            if (StrUtil.isBlankIfStr(data)) {
                throw new BadRequestException("错误的请求报文");
            }
            boolean isReadEnd = data.length < SimplipfyConstant.MAX_HEADER_LENGTH;
            String headerContext = new String(data, "iso-8859-1");
            String bodyContext = null;
            if (headerContext.contains(splitFlag)) {
                bodyContext = headerContext.substring(headerContext.indexOf(splitFlag) + splitFlag.length());
                headerContext = headerContext.substring(0, headerContext.indexOf(splitFlag));
            }
            headerContext += splitFlag;
            String[] headers = headerContext.split("\r\n");
            if (headers.length < 2) {
                throw new BadRequestException("错误的请求报文");
            }
            String line = headers[0];
            while (line.contains("  ")) {
                line = line.replace("  ", " ");
            }
            String[] vanguards = line.trim().split(" ");
            if (vanguards.length != 3) {
                throw new BadRequestException("错误的请求报文");
            }
            request.setMethod(vanguards[0]);
            String requestURI = vanguards[1];
            if (requestURI.contains("?")) {
                int index = requestURI.indexOf("?");
                if (index < requestURI.length() - 1) {
                    request.setQueryString(requestURI.substring(index + 1));
                }
                requestURI = requestURI.substring(0, index);
            }
            request.setRequestURI(requestURI);
            request.setProtocol(vanguards[2]);
            for (int i = 1; i < headers.length; i++) {
                String header = headers[i];
                int index = header.indexOf(":");
                if (index < 1) {
                    throw new BadRequestException("错误的请求头部:" + line);
                }
                String name = header.substring(0, index).trim();
                String value = header.substring(index + 1).trim();
                if (StringUtils.hasNull(name, value)) {
                    continue;
                }
                request.setHeader(name, value);
                if (name.equals("Content-Encoding")) {
                    if (value.contains("gzip")) {
                        request.setGzip(true);
                    }
                }
                if (name.equals("Host")) {
                    String basePath = request.getScheme() + "://" + value;
                    if (requestURI.startsWith(basePath)) {
                        requestURI = requestURI.substring(basePath.length());
                        request.setRequestURI(requestURI);
                    }
                }
                if (name.equals("Content-Length")) {
                    request.setContextLength(Integer.valueOf(value));
                }
            }
            try {
                if (isReadEnd || request.getContextLength() < 1) {
                    if (!StrUtil.isBlankIfStr(bodyContext)) {
                        request.setInputStream(new ByteArrayInputStream(bodyContext.getBytes("iso-8859-1")));
                    }
                    return;
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    byte[] bodyData = bodyContext.getBytes("iso-8859-1");
                    byteArrayOutputStream.write(bodyData);
                    int remainLength = request.getContextLength() - bodyData.length;
                    byte[] remainData = ByteUtils.getBytes(socket.getInputStream(), remainLength);
                    byteArrayOutputStream.write(remainData);
                    request.setInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                } finally {
                    byteArrayOutputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SimplifyException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void buildRequest() throws Exception {
        this.request = new HttpServletRequest();
    }


    @Override
    protected void flush() throws IOException {
        byte[] data = response.getOutputStream().toByteArray();
        if (StrUtil.isBlankIfStr(data)) {
            return;
        }
        socket.getOutputStream().write(data);
    }


}
