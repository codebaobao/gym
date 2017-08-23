package com.portal.controller.event;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.portal.common.Role;
import com.portal.utils.event.EventInfo;
import com.portal.utils.event.EventType;
import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;
import com.portal.utils.thread.AbstractJob;

public class EventPushJob extends AbstractJob {
	private static ILogger logger = LogUtil.getLogger(LogModule.EventPush, EventPushJob.class);
	private boolean stopFlag = false;
	
	@Override
	public void handle() {
		
		while(!stopFlag)
		{
			BlockingQueue<EventInfo> eventQueue = EventPushService.getInstance().getEventQueue();
			try {
				EventInfo event = eventQueue.take();
				logger.info("To push events: " + event.toString());
				List<EventReceiver> receivers = EventPushService.getInstance().getEventReceiverList();
				for(EventReceiver receiver: receivers){
					receiver.onEvent(event);
				}
			} catch (Exception e) {
				logger.warn("", e);
			}
			
		}
	}

}
