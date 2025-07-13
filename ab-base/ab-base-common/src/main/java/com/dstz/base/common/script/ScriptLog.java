package com.dstz.base.common.script;

import java.util.ArrayList;
import java.util.List;

import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.ContextCleanUtils;

import cn.hutool.core.util.StrUtil;

/**
 * <pre>
 * Behavior log during groovy script execution
 * </pre>
 * 
 */
public class ScriptLog {
	/**
	 * Whether the system enables script logging
	 */
	private final static ThreadLocal<Boolean> configOpen = new ThreadLocal<>();
	/**
	 * Whether the thread opens script logging
	 */
	private final static ThreadLocal<Boolean> threadOpen = new ThreadLocal<>();
	/**
	 * Log List
	 */
	private final static ThreadLocal<List<String>> msgs = new ThreadLocal<>();

	public static boolean isOpen() {
		return isOpenScriptLog() && Boolean.TRUE.equals(threadOpen.get());
	}
	
	public static boolean isOpenScriptLog() {
		if (configOpen.get() == null) {
			boolean b = PropertyEnum.SCRIPT_LOG.getPropertyValue(Boolean.class);// 从配置读
			configOpen.set(b);
		}
		
		return Boolean.TRUE.equals(configOpen.get());
	}
	
	
	static {
		ContextCleanUtils.register(ScriptLog::clear, ContextCleanUtils.Phase.values());
	}

	public static void setThreadOpen(boolean b) {
		threadOpen.set(b);
	}
	
	public static void setConfigOpen(boolean b) {
		configOpen.set(b);
	}
	
	public static void openThread() {
		clear();
		threadOpen.set(true);
	}
	
	/**
	 * <pre>
	 * Add log
	 * Parameters support hutool string formatting: ScriptLog.addMsg("Code","Code Description","a={},b={}", a, b)
	 * </pre>
	 * 
	 * @param code
	 *            Code
	 * @param codeDesc
	 *            Code Description
	 * @param desc
	 *            Parameter Description
	 * @param args
	 *            Parameter Description Hutool parameters used
	 */
	public static void addMsg(String code, String codeDesc, String desc, Object... args) {
		if (!isOpen()) {
			return;
		}
		if (msgs.get() == null) {
			msgs.set(new ArrayList<>());
		}
		String str = "{}|{}。{}";
		msgs.get().add(StrUtil.format(str, code, codeDesc, StrUtil.format(desc, args)));
	}

	public static List<String> getMsgs() {
		return msgs.get() == null ? new ArrayList<>() : msgs.get();
	}

	public static void clear() {
		configOpen.remove();
		threadOpen.remove();
		msgs.remove();
	}

	public static List<String> getMsgsAndClear() {
		List<String> list = getMsgs();
		clear();
		return list;
	}
}
