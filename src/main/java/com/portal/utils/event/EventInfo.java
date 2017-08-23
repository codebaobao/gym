package com.portal.utils.event;


public class EventInfo {
	private EventType action;
	private EventSource source;
	

	public EventInfo(EventType action)
	{
		this.action = action;
	}
	
	public EventType getAction() {
		return action;
	}

	public EventSource getSource() {
		return source;
	}

	public void setSource(EventSource source) {
		this.source = source;
	}

	public void setAction(EventType action) {
		this.action = action;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ArchiveEvent=[");
		sb.append("action=").append(action.toString());
		sb.append(", source=").append(source == null ? "null" : source.toString());
		sb.append("]");
		
		return sb.toString();
	}
	
}
