package com.dstz.springboot.autoconfigure.idgen;

import cn.hutool.core.date.SystemClock;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;

/**
 * ID Generator Configuration
 *
 */
@ConfigurationProperties(prefix = "ab.id-generator")
public class IdGeneratorProperties {

	/**
	 * Generator Types
	 */
	private IdGeneratorType type = IdGeneratorType.SNOWFLAKE;

	/**
	 * Snowflake Algorithm Configuration
	 */
	private Snowflake snowflake = new Snowflake();

	public IdGeneratorType getType() {
		return type;
	}

	public void setType(IdGeneratorType type) {
		this.type = type;
	}

	public Snowflake getSnowflake() {
		return snowflake;
	}

	public void setSnowflake(Snowflake snowflake) {
		this.snowflake = snowflake;
	}

	public static class Snowflake {

		/**
		 * Initialization time starting point (null means the default starting date). Later modifications will cause ID duplication. If you want to modify workerId and dataCenterId, use with caution.
		 */
		private Date epochDate;

		/**
		 * Data Center ID
		 */
		private Long dataCenterId;

		/**
		 * Worker node ID
		 */
		private Long workerId;

		/**
		 * Whether to use {@link SystemClock} get the current timestamp
		 */
		private Boolean isUseSystemClock = Boolean.FALSE;

		/**
		 * Default callback time, 2S
		 */
		private Long timeOffset = cn.hutool.core.lang.Snowflake.DEFAULT_TIME_OFFSET;

		public Date getEpochDate() {
			return epochDate;
		}

		public void setEpochDate(Date epochDate) {
			this.epochDate = epochDate;
		}

		public Long getDataCenterId() {
			return dataCenterId;
		}

		public void setDataCenterId(Long dataCenterId) {
			this.dataCenterId = dataCenterId;
		}

		public Long getWorkerId() {
			return workerId;
		}

		public void setWorkerId(Long workerId) {
			this.workerId = workerId;
		}

		public Boolean getUseSystemClock() {
			return isUseSystemClock;
		}

		public void setUseSystemClock(Boolean useSystemClock) {
			isUseSystemClock = useSystemClock;
		}

		public Long getTimeOffset() {
			return timeOffset;
		}

		public void setTimeOffset(Long timeOffset) {
			this.timeOffset = timeOffset;
		}
	}
}
