package com.git.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author authorZhao
 */
@ConfigurationProperties(prefix = "open.blog.file")
@Data
@Configuration
public class FileProperties {
	/**
	 * win上传路径
	 */
	private String windowUpLoadPathPrefix = "E:/upLoad/fileDownLoad/";

	/**
	 * linux上传路径
	 */
	private String linuxUpLoadPathPrefix = "/usr/local/upLoad/";

	/**
	 * 文件大小，单位byte
	 */
	public long fileSize = 10*1024*1024;

	/**
	 * 分类图标大小，单位byte
	 */
	public long iconFileSize = 2*1024*1024;

	/**
	 * 二级目录，文件类型
	 */
	private String sufPath = "file/";


}
