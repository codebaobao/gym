package com.portal.controller.event;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import com.portal.utils.event.EventInfo;
import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;

public class EventPushService{
	private static ILogger logger = LogUtil.getLogger(LogModule.EventPush, EventPushService.class);

	private static EventPushService _instance;

	private BlockingQueue<EventInfo> eventQueue;
	
	private BlockingQueue<EventInfo> eventServerQueue;

	private List<EventReceiver> listeners;

	private EventPushJob pushJob;
	
	private NettyJob nettyJob;
	
	private EventPushByNettyJob eventPushByNettyJob;

	@PostConstruct
	public void init() {
		eventQueue = new LinkedBlockingQueue<EventInfo>();
		eventServerQueue = new LinkedBlockingQueue<EventInfo>();


		listeners = new CopyOnWriteArrayList<EventReceiver>();
		pushJob = new EventPushJob();
		pushJob.setJobName("EventPushJob");
		pushJob.execute();
		
		
		nettyJob = new NettyJob();
		nettyJob.setJobName("NettyJob");
		nettyJob.execute();
		eventPushByNettyJob = new EventPushByNettyJob();
		eventPushByNettyJob.setJobName("EventPushByNettyJob");
		eventPushByNettyJob.execute();

		logger.info("EventPushService initialized.");
	}

	public BlockingQueue<EventInfo> getEventQueue() {
		return eventQueue;
	}
	
	public BlockingQueue<EventInfo> getEventServerQueue() {
		return eventServerQueue;
	}

	public void addEventReceiver(EventReceiver receiver) {
		listeners.add(receiver);
	}

	public void removeEventReceiver(EventReceiver receiver) {
		listeners.remove(receiver);
	}

	public List<EventReceiver> getEventReceiverList() {
		return listeners;
	}

	public void addEvent(EventInfo event) {
		eventQueue.add(event);
		eventServerQueue.add(event);
	}

	public static EventPushService getInstance() {
		if(_instance == null){
			_instance = new EventPushService();
		}
		return _instance;
	}
}
