package com.portal.utils.event;

import com.portal.controller.event.EventPushService;

public class EventCallbackManager {
	
	public static void notifyListeners(EventType type, EventSourceType sourceType, Object detail){
		EventInfo event = new EventInfo(type);
		EventSource cs = new EventSource();
		cs.setDetail(detail);
		cs.setType(sourceType);
		event.setSource(cs);
		EventPushService.getInstance().addEvent(event);
	}
	
}
