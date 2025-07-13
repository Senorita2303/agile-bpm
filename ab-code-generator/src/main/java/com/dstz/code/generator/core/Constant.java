package com.dstz.code.generator.core;

import cn.hutool.core.util.StrUtil;

import java.io.File;

public class Constant {

	static final String DEFAULT_OUTPUT_DIR = getDefaultOutputDir();

	private static String getDefaultOutputDir() {
		File currentDir = new File(Constant.class.getResource("/").getFile());
		if (currentDir.isDirectory()) {
			return new File(currentDir.getParentFile(),
					StrUtil.join(File.separator, "code-generator", "src", "main", "java")).getAbsolutePath();
		} else {
			return StrUtil.join(File.separator, System.getProperty("user.dir"), "code-generator", "src", "main",
					"java");
		}
	}

}
