package com.dstz.cms.api.event;

import org.springframework.context.ApplicationEvent;

import java.util.Collection;
import java.util.List;

public class RemoveNotifyTypeEvent extends ApplicationEvent {

	public RemoveNotifyTypeEvent(Collection<?> ids) {
		super(ids);
	}

	@Override
	public List<String> getSource() {
		return (List<String>) super.getSource();
	}
}