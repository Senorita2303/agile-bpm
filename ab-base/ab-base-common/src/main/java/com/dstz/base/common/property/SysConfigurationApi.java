package com.dstz.base.common.property;

public interface SysConfigurationApi {
    /**
     * Get the configuration object according to the code
     * @param code Configuration code
     * @param typeClass The type to be returned
     * @return
     */
    <T> T getConfigByCode(String code, Class<T> typeClass);

    String getConfByCode(String code);

    /**
     * Save Configuration
     *
     * @param json Configuration json
     */
    void saveConf(String json);

    /**
     * Delete configuration by encoding
     *
     * @param code Configuration Encoding
     */
    void deleteByCode(String code);
}
