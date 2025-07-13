package com.dstz.code.generator.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.dstz.code.generator.AbCodeGeneratorProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * Code Generator
 * 
 */
@Component
public class AbCodeGenerator {

	private final DataSourceProperties dataSourceProperties;

	private final AbCodeGeneratorProperties codeGeneratorProperties;

	public AbCodeGenerator(DataSourceProperties dataSourceProperties,
			AbCodeGeneratorProperties codeGeneratorProperties) {
		this.dataSourceProperties = dataSourceProperties;
		this.codeGeneratorProperties = codeGeneratorProperties;
	}

	public void run(AbCodeGeneratorModel codeGeneratorModel) {
		new Runner(codeGeneratorModel).run();
	}

	private final class Runner implements Runnable {

		private final AbCodeGeneratorModel codeGeneratorModel;

		public Runner(AbCodeGeneratorModel codeGeneratorModel) {
			this.codeGeneratorModel = codeGeneratorModel;
		}

		@Override
		public void run() {
			AutoGenerator autoGenerator = new AutoGenerator(dataSourceConfig());
			// Global Configuration
			autoGenerator.global(globalConfig());
			// Template Configuration
			autoGenerator.template(templateConfig());
			// Package Information
			autoGenerator.packageInfo(packageConfig(codeGeneratorModel.getPackageName()));

			// Policy Configuration
			StrategyConfig.Builder strategyConfigBuilder = strategyConfig();
			if (ArrayUtil.isNotEmpty(codeGeneratorModel.getIncludeTable())) {
				strategyConfigBuilder.addInclude(codeGeneratorModel.getIncludeTable());
			}
			if (ArrayUtil.isNotEmpty(codeGeneratorModel.getExcludeTable())) {
				strategyConfigBuilder.addExclude(codeGeneratorModel.getExcludeTable());
			}

			// Ignore table prefix configuration
			strategyConfigBuilder.addTablePrefix("");

			autoGenerator.strategy(strategyConfigBuilder.build());

			// Generate Execution
			autoGenerator.execute(new FreemarkerTemplateEngine());

			moveMapperXml(autoGenerator.getConfig().getPathInfo());
		}

		private void moveMapperXml(Map<OutputFile, String> pathInfo) {
			final String searchStr = StrUtil.join(File.separator, "src", "main", "java");
			String mapperXmlPath = pathInfo.get(OutputFile.xml);
			System.out.println(mapperXmlPath);
			int index = mapperXmlPath.lastIndexOf(searchStr);
			File mapperXmlResourceDir = new File(mapperXmlPath.substring(0, index)
					+ searchStr.replace("java", "resources") + mapperXmlPath.substring(index + searchStr.length()));
			if (!mapperXmlResourceDir.exists()) {
				mapperXmlResourceDir.mkdirs();
			}

			// Migrate files
			for (File file : new File(mapperXmlPath).listFiles((dir, name) -> name.endsWith(".xml"))) {
				FileUtil.move(file, mapperXmlResourceDir, true);
			}
		}

		GlobalConfig globalConfig() {
			GlobalConfig.Builder builder = new GlobalConfig.Builder();
			// Overwrite generated files
			builder.fileOverride();
			// Disable opening of output directory
			builder.disableOpenDir();
			// Author Name
			builder.author(codeGeneratorModel.getAuthor());
			// Time strategy
			builder.dateType(DateType.ONLY_DATE);
			// Annotation Date
			builder.commentDate("yyyy-MM-dd");

			File rootDir;
			if (StrUtil.isNotBlank(codeGeneratorModel.getOutputDir())) {
				rootDir = new File(codeGeneratorModel.getOutputDir());
			} else {
				rootDir = new File(getClass().getResource("/").getFile()).getParentFile();
			}

			// Specifying the Output Directory
			File outputDir = new File(rootDir, StrUtil.join(File.separator, "code-generator", "src", "main", "java"));
			if (!outputDir.exists()) {
				outputDir.mkdirs();
			}
			builder.outputDir(outputDir.getAbsolutePath());
			return builder.build();
		}

		DataSourceConfig dataSourceConfig() {
			DataSourceConfig.Builder builder = new DataSourceConfig.Builder(dataSourceProperties.getUrl(),
					dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
			// Database Query
			//        builder.dbQuery();
			// Database schema (applicable to some databases)
			//        builder.schema();
			// Database Type Converter
			//        builder.typeConvert();
			// Database Keyword Processor
			//        builder.keyWordsHandler();
			return builder.build();
		}

		TemplateConfig templateConfig() {
			TemplateConfig.Builder builder = new TemplateConfig.Builder();
			builder.service("/templates/manager.java");
			builder.serviceImpl("/templates/managerImpl.java");
			return builder.build();
		}

		PackageConfig packageConfig(String packageName) {
			PackageConfig.Builder builder = new PackageConfig.Builder();
			// Parent package name
			builder.parent(packageName);
			// manger
			builder.service("manager");
			builder.serviceImpl("manager.impl");
			builder.xml("mapper");
			return builder.build();
		}

		StrategyConfig.Builder strategyConfig() {
			StrategyConfig.Builder builder = new StrategyConfig.Builder();

			// Enable Skip View
			builder.enableSkipView();

			// Entity Field Population
			Column[] entityTableFills = new Column[] { new Column("create_by_", FieldFill.INSERT),
					new Column("creator_", FieldFill.INSERT), new Column("create_org_id_", FieldFill.INSERT),
					new Column("create_time_", FieldFill.INSERT), new Column("update_by_", FieldFill.INSERT_UPDATE),
					new Column("updater_", FieldFill.INSERT_UPDATE),
					new Column("update_time_", FieldFill.INSERT_UPDATE), new Column("rev_", FieldFill.INSERT) };

			builder

					// Entity policy configuration
					.entityBuilder()
					// Disable generation of serialVersionUID
					.disableSerialVersionUID()
					// Open chain model
					//                .enableChainModel()
					// Open Boolean Type field removed is Prefix
					.enableRemoveIsPrefix()
					// Enable generating field annotations when generating entities
					.enableTableFieldAnnotation()
					// Optimistic lock field name (database)
					.versionColumnName(codeGeneratorProperties.getEntity().getVersionColumnName())
					// Optimistic lock attribute name (entity)
					.versionPropertyName(codeGeneratorProperties.getEntity().getVersionPropertyName())
					// Enable generation of field constants
					//                .enableColumnConstant()
					// Enable ActiveRecord Model
					.enableActiveRecord()
					// Set the parent class
					.superClass(codeGeneratorProperties.getEntity().getSuperClass())
					// Add parent class public fields
					//                .addSuperEntityColumns("id_", "create_by_", "create_time_", "update_by_", "updater_", "update_time_", "rev")
					// Add table field population
					.addTableFills(entityTableFills)
					// Global primary key type
					.idType(IdType.ASSIGN_ID);

			// manager Policy Configuration
			builder.serviceBuilder()
					// Set the manager interface parent class
					.superServiceClass(codeGeneratorProperties.getManager().getInterfaceClass())
					// Set the manager implementation class parent class
					.superServiceImplClass(codeGeneratorProperties.getManager().getInterfaceImplClass())
					.formatServiceFileName(codeGeneratorProperties.getManager().getInterfaceFileNameFormat())
					.formatServiceImplFileName(codeGeneratorProperties.getManager().getInterfaceImplFileNameFormat());

			// Mapper Policy Configuration
			builder.mapperBuilder()
					// Set the parent class
					.superClass(codeGeneratorProperties.getMapper().getSuperClass())
					// Enable @Mapper annotation
					.enableMapperAnnotation().enableBaseResultMap();

			// Controller Policy Configuration
			builder.controllerBuilder()
					// Turn on camel case to hyphen conversion
					//                .enableHyphenStyle()
					// Enable generation of @RestController controllers
					.enableRestStyle().superClass(codeGeneratorProperties.getController().getSuperClass());

			return builder;
		}
	}
}
