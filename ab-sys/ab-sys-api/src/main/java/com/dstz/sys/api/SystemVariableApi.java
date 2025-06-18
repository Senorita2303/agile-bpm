package com.dstz.sys.api;

import java.util.Map;

/**
 * @Name SystemVariableApi
 * @description: Get system variable interface
 */
public interface SystemVariableApi {

    /**
     * Get all system constants
     * key  key such as： currentUserId，value：Get the function of the variable, execute the function, and get the real value
     *
     * This map is in lazy loading mode, it cannot be traversed, it cannot be determined whether it is included, and can only get the value in it
     * @return
     */
    Map<String,Object> getVariableMap();


    /**
     * Get the system constants contained in the specified string
     * key  key such as： currentUserId，value：16452347899
     * Only variables in the form of ${xxx} are supported
     * @param str The specified string
     * @return System constants contained in string
     */
    Map<String,Object> getVariableMap(String str);

    /**
     * Determine whether the string contains scripting
     *
     * @param value The string to be judged
     *
     * @return boolean Is it included true Includes false Not included
     */
    boolean containScript(String value);

    /**
     * <pre>
     * Returns the actual value of a variable
     * ps: If it is a script, the return may be other types, so it is object
     *
     * </pre>
     *
     * @param val ${valName} #{valName}
     * @return
     */
    Object getVariableValue(Object val);
}
