package com.dstz.base.common.script;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.events.AbRequestLogEvent;
import com.dstz.base.common.requestlog.AbRequestLog;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Script execution log
 *
 */
@Component
public class ScriptLogListener implements ApplicationListener<AbRequestLogEvent> {

	@Override
	public void onApplicationEvent(AbRequestLogEvent event) {
		if (AbRequestLogEvent.EventType.POST_PROCESS.equals(event.getEventType()) && ScriptLog.isOpenScriptLog()) {
			AbRequestLog requestLog = event.getRequestLog();
			// If there is an exception, set the script log in the exception encapsulation class
			if(requestLog.getException() != null) return ;
			
			Object respones =  requestLog.getResponseBody();
			if(respones instanceof ApiResponse) {
				List<String> msgs = ScriptLog.getMsgsAndClear();
				// Set the return value
				if(CollUtil.isNotEmpty(msgs)) {
					((ApiResponse<?>) respones).setScriptLog(StrUtil.join("\n", msgs));
				}
			}
		}
	}
}
