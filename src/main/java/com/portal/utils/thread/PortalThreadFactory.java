package com.portal.utils.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
class PortalThreadFactory implements ThreadFactory
{
	private final static String PORTAL_THREADS = "portal-thread-group";
	private final static String PORTAL_THREAD_PREFIX = "portal-thread-";
	
	private static AtomicInteger threadNamePostfix = new AtomicInteger();;
	
	private	static PortalThreadFactory _instance;
	private ThreadGroup thGroup;
	
	private PortalThreadFactory()
	{
		thGroup = new ThreadGroup(PORTAL_THREADS);
	}
	
	public synchronized static PortalThreadFactory getInstance()
	{
		if(_instance == null)
		{
			_instance = new PortalThreadFactory();
		}
		return _instance;
	}
	
	@Override
	public Thread newThread(Runnable arg0) 
	{
		return new Thread(thGroup, arg0, PORTAL_THREAD_PREFIX + threadNamePostfix.incrementAndGet());
	}
	
}
