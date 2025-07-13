package com.dstz.auth.authentication.api;

import com.dstz.auth.authentication.vo.SysApplicationVO;

import java.util.List;

/**
 * System application interface
 *
 */
public interface SysApplicationApi{

	/**
	 * Get system application by code
	 *
	 * @param code Code
	 * @return System Application
	 */
	SysApplicationVO getByCode(String code);

	/**
	 * Get all application codes
	 *
	 * @return All application code
	 */
	List<String> getAllCode();

	/**
	 * Get the currently available mobile main application
	 * @return
	 */
	SysApplicationVO getEnabledMobileApp();
}
