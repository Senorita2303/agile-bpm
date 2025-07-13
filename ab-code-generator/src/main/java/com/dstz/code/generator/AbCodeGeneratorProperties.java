package com.dstz.code.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Generator Configuration
 *
 */
@ConfigurationProperties(prefix = "ab.code-generator")
public class AbCodeGeneratorProperties {

	/**
	 * Entity Configuration
	 */
	private final EntityProperties entity = new EntityProperties();
	/**
	 * Mapper Configuration
	 */
	private final MapperProperties mapper = new MapperProperties();
	/**
	 * Manager Configuration
	 */
	private final ManagerProperties manager = new ManagerProperties();
	/**
	 * Controller Configuration
	 */
	private final ControllerProperties controller = new ControllerProperties();
	/**
	 * Enable GUI
	 */
	private Boolean enableGui = Boolean.FALSE;

	public Boolean getEnableGui() {
		return enableGui;
	}

	public void setEnableGui(Boolean enableGui) {
		this.enableGui = enableGui;
	}

	public EntityProperties getEntity() {
		return entity;
	}

	public MapperProperties getMapper() {
		return mapper;
	}

	public ManagerProperties getManager() {
		return manager;
	}

	public ControllerProperties getController() {
		return controller;
	}

	/**
	 * Entity Configuration
	 */
	public static final class EntityProperties {

		/**
		 * Base Class
		 */
		private String superClass = "com.dstz.base.entity.AbModel";

		/**
		 * Optimistic lock column name (database)
		 */
		private String versionColumnName = "rev_";

		/**
		 * Optimistic lock attribute name (entity)
		 */
		private String versionPropertyName = "rev";

		/**
		 * Primary key type
		 */
		private IdType idType = IdType.ASSIGN_ID;

		public String getSuperClass() {
			return superClass;
		}

		public void setSuperClass(String superClass) {
			this.superClass = superClass;
		}

		public String getVersionColumnName() {
			return versionColumnName;
		}

		public void setVersionColumnName(String versionColumnName) {
			this.versionColumnName = versionColumnName;
		}

		public String getVersionPropertyName() {
			return versionPropertyName;
		}

		public void setVersionPropertyName(String versionPropertyName) {
			this.versionPropertyName = versionPropertyName;
		}

		public IdType getIdType() {
			return idType;
		}

		public void setIdType(IdType idType) {
			this.idType = idType;
		}
	}

	/**
	 * Mapper Configuration
	 */
	public static final class MapperProperties {

		/**
		 * Base Class
		 */
		private String superClass = "com.dstz.base.mapper.AbBaseMapper";

		public String getSuperClass() {
			return superClass;
		}

		public void setSuperClass(String superClass) {
			this.superClass = superClass;
		}
	}

	/**
	 * Manager Configuration
	 */
	public static final class ManagerProperties {

		/**
		 * Common interface class
		 */
		private String interfaceClass = "com.dstz.base.manager.AbBaseManager";

		/**
		 * Common interface implementation class
		 */
		private String interfaceImplClass = "com.dstz.base.manager.impl.AbBaseManagerImpl";

		/**
		 * Interface file name format, %s: entity name
		 */
		private String interfaceFileNameFormat = "%sManager";

		/**
		 * Interface implementation file name formatting, %s: entity name
		 */
		private String interfaceImplFileNameFormat = "%sManagerImpl";

		public String getInterfaceClass() {
			return interfaceClass;
		}

		public void setInterfaceClass(String interfaceClass) {
			this.interfaceClass = interfaceClass;
		}

		public String getInterfaceImplClass() {
			return interfaceImplClass;
		}

		public void setInterfaceImplClass(String interfaceImplClass) {
			this.interfaceImplClass = interfaceImplClass;
		}

		public String getInterfaceFileNameFormat() {
			return interfaceFileNameFormat;
		}

		public void setInterfaceFileNameFormat(String interfaceFileNameFormat) {
			this.interfaceFileNameFormat = interfaceFileNameFormat;
		}

		public String getInterfaceImplFileNameFormat() {
			return interfaceImplFileNameFormat;
		}

		public void setInterfaceImplFileNameFormat(String interfaceImplFileNameFormat) {
			this.interfaceImplFileNameFormat = interfaceImplFileNameFormat;
		}
	}

	/**
	 * Controller Configuration
	 */
	public static final class ControllerProperties {

		/**
		 * Base Class
		 */
		private String superClass = "com.dstz.base.web.controller.AbCrudController";

		/**
		 * Enable @RestController annotation
		 */
		private Boolean enableRestStyle = Boolean.TRUE;

		public String getSuperClass() {
			return superClass;
		}

		public void setSuperClass(String superClass) {
			this.superClass = superClass;
		}

		public Boolean getEnableRestStyle() {
			return enableRestStyle;
		}

		public void setEnableRestStyle(Boolean enableRestStyle) {
			this.enableRestStyle = enableRestStyle;
		}
	}
}
