package com.portal.utils.event;

public class EventSource {
	private EventSourceType type;
	private Object detail;

	public Object getDetail() {
		return detail;
	}

	public void setDetail(Object detail) {
		this.detail = detail;
	}
	
	public EventSourceType getType() {
		return type;
	}

	public void setType(EventSourceType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "EventSource [type=" + type + ", detail=" + detail + "]";
	}
	
}
