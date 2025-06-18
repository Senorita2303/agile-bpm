package com.dstz.code.generator.core;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Objects;
import java.util.Scanner;

public class AbCodeGeneratorCommandLine {

	private final AbCodeGenerator codeGenerator;

	public AbCodeGeneratorCommandLine(AbCodeGenerator codeGenerator) {
		this.codeGenerator = codeGenerator;
	}

	public void run() {
		AbCodeGeneratorModel codeGeneratorModel = new AbCodeGeneratorModel();
		Scanner scanner = new Scanner(System.in);

		System.out.println(StrUtil.center("Fill in the information", 30, '*'));

		System.out.print("Save directory (optional):");
		codeGeneratorModel.setOutputDir(StrUtil.trim(scanner.nextLine()));

		System.out.print("Author (optional):");
		codeGeneratorModel.setAuthor(StrUtil.trim(scanner.nextLine()));

		for (int i = 0;; i++) {
			if (i > 0) {
				System.out.println(StrUtil.center("Fill in the information", 30, '*'));
				codeGeneratorModel.setPackageName(null);
				codeGeneratorModel.setIncludeTable(null);
				codeGeneratorModel.setExcludeTable(null);
			}

			// Fill in the package name
			while (StrUtil.isBlank(codeGeneratorModel.getPackageName())) {
				System.out.print("Package name (required):");
				codeGeneratorModel.setPackageName(StrUtil.trim(scanner.nextLine()));
			}

			System.out.print("Specify table (optional, multiple tables separated by commas (,)):");
			String line = StrUtil.trimToNull(scanner.nextLine());
			codeGeneratorModel.setIncludeTable(Objects.isNull(line) ? null : line.split(StrUtil.COMMA));

			if (ArrayUtil.isEmpty(codeGeneratorModel.getIncludeTable())) {
				System.out.print("Exclude tables (optional, multiple tables separated by commas (,)):");
				line = StrUtil.trimToNull(scanner.nextLine());
				codeGeneratorModel.setExcludeTable(Objects.isNull(line) ? null : line.split(StrUtil.COMMA));
			}

			System.out.println(StrUtil.center("Generate information", 30, '*'));
			System.out.printf("Save directory: %s\n", codeGeneratorModel.getOutputDir());
			System.out.printf("Author: %s\n", codeGeneratorModel.getAuthor());
			System.out.printf("Package name: %s\n", codeGeneratorModel.getPackageName());
			System.out.printf("Specified table: %s\n",
					StrUtil.nullToEmpty(ArrayUtil.join(codeGeneratorModel.getIncludeTable(), StrUtil.COMMA)));
			System.out.printf("Exclusion table: %s\n",
					StrUtil.nullToEmpty(ArrayUtil.join(codeGeneratorModel.getExcludeTable(), StrUtil.COMMA)));

			System.out.println(StrUtil.repeat('*', 30));

			System.out.print("(Y: Confirm N: Re-fill E: Exit):");

			String cmd = StrUtil.trimToEmpty(scanner.nextLine());
			if (StrUtil.equalsIgnoreCase(cmd, "Y")) {
				invokeGenerator(codeGeneratorModel);
				System.out.println(StrUtil.repeat('*', 30));
				System.out.print("Do you want to continue (Y/N):");
				if (!StrUtil.equalsIgnoreCase(StrUtil.trimToEmpty(scanner.nextLine()), "Y")) {
					System.exit(0);
				}
			} else if (StrUtil.equalsIgnoreCase(cmd, "E")) {
				System.exit(0);
			}
		}
	}

	private void invokeGenerator(AbCodeGeneratorModel codeGeneratorModel) {
		try {
			codeGenerator.run(codeGeneratorModel);
		} catch (AlertMessageException e) {
			System.err.println(StrUtil.center("Error message", 30, '*'));
			System.out.println(e.getMessage());
		}
	}

}
