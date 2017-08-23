package com.portal.controller.event;

import com.alibaba.fastjson.JSON;
import com.portal.common.Role;
import com.portal.utils.event.EventInfo;
import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicLong;

public class EventReceiver {
	private static ILogger logger = LogUtil.getLogger(LogModule.EventPush, EventReceiver.class);
	
	private static AtomicLong eventId;
	private String userId;
	private Role userRole;
	private long id;
	private AsyncContext ac;
	private String sessionId;
	
	public EventReceiver(long id){
		this.id = id;
	}
	
	public EventReceiver(AsyncContext ac){
		this.ac = ac;
	}
	
	public void onEvent(EventInfo event)
	{
    
		String eventType = event.getAction().name();
		String eventAction = event.getSource().getType().name();
		
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"eventType\": ").append("\"").append(eventType).append("\"").append(",");
        sb.append("\"eventAction\": ").append("\"").append(eventAction).append("\"").append(",");
        sb.append("\"eventId\": ").append("\"").append(eventId.getAndIncrement()).append("\"");
        if(event.getSource().getDetail() != null)
        	sb.append(", \"detail\":").append(JSON.toJSONString(event.getSource().getDetail()));
        sb.append("}");
        
        writeEvent("up_message", sb.toString());
	        
	}
	
	private void writeEvent(String event, String message) {
		
		final ServletResponse res = ac.getResponse();
        PrintWriter writer;
        try {
			writer = res.getWriter();
			char[] pad = new char[2048]; 
			writer.write(pad);
			writer.write("event: " + event + "\n\n");
			writer.write("data: " + message + "\n\n");
			if (writer.checkError()) { 
				//checkError calls flush, and flush() does not throw IOException
				EventPushService.getInstance().removeEventReceiver(this);
				logger.info("To remove listener " + id);
			} else {
				//System.out.println("Successfully write to listener " + id);
			}
		} catch (Exception e) {
			EventPushService.getInstance().removeEventReceiver(this);
			logger.warn("writeEvent Exception", e);
		}
        
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Role getUserRole() {
		return userRole;
	}

	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "EventReceiver [userId=" + userId + ", userRole=" + userRole
				+ ", id=" + id + ", sessionId=" + sessionId + "]";
	}

	static{
		eventId = new AtomicLong(System.currentTimeMillis());
	}
}
