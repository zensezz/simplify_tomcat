package cn.zensezz.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class MultipartFile {

	private String paramName;

	private String fileName;

	private byte[] fileContext;

	private String suffix;

	private String contextType;

	public void setFileName(String fileName) {
		this.fileName = fileName;
		this.suffix=getSuffix(fileName);
	}
	private String getSuffix(String fileName) {
		if (StrUtil.isBlankIfStr(fileName)) {
			return null;
		}
		String[] strs = fileName.split("\\.");
		return strs[strs.length - 1].toLowerCase();
	}
}