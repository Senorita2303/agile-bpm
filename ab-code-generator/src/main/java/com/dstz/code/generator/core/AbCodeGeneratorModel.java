package com.dstz.code.generator.core;

import cn.hutool.core.util.StrUtil;

/**
 * Generator parameter object
 *
 */
public class AbCodeGeneratorModel {

	/**
	 * Output Directory
	 */
	private String outputDir;

	/**
	 * author
	 */
	private String author;

	/**
	 * Package Name
	 */
	private String packageName;

	/**
	 * Specify table, supports wildcards
	 */
	private String[] includeTable;

	/**
	 * Exclusion table, supports wildcards
	 */
	private String[] excludeTable;

	public String getOutputDir() {
		return StrUtil.blankToDefault(outputDir, Constant.DEFAULT_OUTPUT_DIR);
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public String getAuthor() {
		return StrUtil.blankToDefault(author, System.getProperty("user.name"));
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String[] getIncludeTable() {
		return includeTable;
	}

	public void setIncludeTable(String[] includeTable) {
		this.includeTable = includeTable;
	}

	public String[] getExcludeTable() {
		return excludeTable;
	}

	public void setExcludeTable(String[] excludeTable) {
		this.excludeTable = excludeTable;
	}
}
