package com.dstz.component.upload.api;

import java.io.InputStream;

/**
 * <pre>
 * Description: The upload stream interface of the system
 * </pre>
 *
 */
public interface IUploader {
	/**
	 * <pre>
	 * type
	 * </pre>
	 * 
	 * @return
	 */
	String type();


	/**
	 * <pre>
	 * Upload Stream
	 * </pre>
	 * 
	 * @param is
	 *            flow
	 * @param name
	 *            Stream Name
	 * @param type
	 * 			  File Type
	 * @return Flow path, when taking, the corresponding flow can be obtained by this path
	 */
	String upload(InputStream is, String name,String type);


	/**
	 * <pre>
	 * Get the stream according to the stream path
	 * </pre>
	 * 
	 * @param path
	 * @return
	 */
	InputStream take(String path);
	
	/**
	 * <pre>
	 * Delete a Flow
	 * </pre>
	 * 
	 * @param path
	 */
	void remove(String path);
}
