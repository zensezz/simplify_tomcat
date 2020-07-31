package cn.zensezz.aop;

import cn.hutool.core.util.StrUtil;
import cn.zensezz.entity.MultipartFile;
import cn.zensezz.constant.SimplipfyConstant;
import cn.zensezz.util.ByteUtils;
import cn.zensezz.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParamentAop {

	public static Map<String, List<Object>> buildGeneralParams(String queryString) {
		if (StrUtil.isBlankIfStr(queryString)) {
			return new HashMap<String, List<Object>>();
		}
		String[] lines = queryString.split("&");
		Map<String, List<Object>> params = new HashMap<String, List<Object>>();
		for (String line : lines) {
			try {
				int index = line.indexOf("=");
				if (index < 1 || index == line.length() - 1) {
					continue;
				}
				String paramName = line.substring(0, index);
				String paramValue = URLDecoder.decode(line.substring(index + 1), SimplipfyConstant.ENCODE);
				if (!params.containsKey(paramName)) {
					List<Object> paramValues = new ArrayList<Object>();
					params.put(paramName, paramValues);
				}
				params.get(paramName).add(paramValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return params;
	}
	public static Map<String, List<Object>> buildMultipartParams(byte[] data, String boundary) {
		if (StrUtil.isBlankIfStr(data)) {
			return new HashMap<String, List<Object>>();
		}
		Map<String, List<Object>> resultMap = new HashMap<String, List<Object>>();
		try {
			String context = new String(data, "ISO-8859-1");
			String boundaryTag = "--" + boundary;
			String[] paramContexts = context.split(boundaryTag);
			for (String paramContext : paramContexts) {
				MultipartFile multipartFile=buildMultipartFile(paramContext);
				if(StrUtil.isBlankIfStr(multipartFile)){
					continue;
				}
				if (!resultMap.containsKey(multipartFile.getParamName())) {
					List<Object> files = new ArrayList<Object>();
					resultMap.put(multipartFile.getParamName(), files);
				}
				resultMap.get(multipartFile.getParamName()).add(multipartFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	private static MultipartFile buildMultipartFile(String paramContext){
		if (StrUtil.isBlankIfStr(paramContext)) {
			return null;
		}
		ByteArrayInputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(paramContext.trim().getBytes("ISO-8859-1"));
			String line = ByteUtils.readLineString(inputStream).trim();
			String contextType = "text/plain";
			Map<String, String> buildMap = buildParaMap(line);
			if (StrUtil.isBlankIfStr(buildMap)) {
				return null;
			}
			String paramName=buildMap.get("name");
			if (StrUtil.isBlankIfStr(paramName)) {
				return null;
			}
			line = ByteUtils.readLineString(inputStream).trim();
			if (line.contains("Content-Type")) {
				contextType = line.substring(line.indexOf(":") + 1).trim();
			}
			while (!StrUtil.isBlankIfStr(line)) {
				line = ByteUtils.readLineString(inputStream).trim();
			}
			byte[] value = ByteUtils.getBytes(inputStream);
			MultipartFile multipartFile = new MultipartFile();
			multipartFile.setContextType(contextType);
			multipartFile.setFileContext(value);
			multipartFile.setParamName(buildMap.get("name"));
			multipartFile.setFileName(buildMap.get("filename"));
			return multipartFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static Map<String, String> buildParaMap(String context) {
		if (context.contains(":")) {
			context = context.substring(context.indexOf(":") + 1);
		}
		String[] lines = context.split("; ");
		Map<String, String> paraMap = new HashMap<String, String>();
		for (String line : lines) {
			if (!line.contains("=")) {
				continue;
			}
			String name = line.substring(0, line.indexOf("=")).trim();
			String value = line.substring(line.indexOf("=") + 1).replace("\"", "").trim();
			if (StringUtils.hasNull(name, value)) {
				continue;
			}
			paraMap.put(name, value);
		}
		if (StrUtil.isBlankIfStr(paraMap)) {
			return null;
		}
		return paraMap;
	}

}

