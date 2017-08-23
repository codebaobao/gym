package com.portal.utils.event;

public enum EventType {
	UserChanged,
	OrderChange
	;

	public EventType[] getAllActions() 
	{
		return EventType.values();
	}
}
