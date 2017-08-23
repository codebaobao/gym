package com.portal.utils.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.portal.config.TaskThreadPoolConfig;

public class PortalThreadService
{
	private static PortalThreadService _instance;
	
	@Autowired
	private TaskThreadPoolConfig config;
	
	private ThreadPoolExecutor corePoolExec;
	private ScheduledThreadPoolExecutor scheduledPoolExec;
	
	private PortalThreadService()
	{
		int coreSize = config.getCorePoolSize();
		int maxSize = config.getCorePoolMaxSize();
		long keepAliveTime = config.getThreadTimeout();
		int scheduledPoolSize = config.getScheduledPoolSize();
				
		corePoolExec = new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), PortalThreadFactory.getInstance(), new ThreadPoolExecutor.CallerRunsPolicy());					
		scheduledPoolExec = new ScheduledThreadPoolExecutor(scheduledPoolSize, PortalThreadFactory.getInstance(), new ThreadPoolExecutor.CallerRunsPolicy());		
		scheduledPoolExec.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);
		scheduledPoolExec.allowCoreThreadTimeOut(true);
		
		PortalMonitorJob job = new PortalMonitorJob();
		corePoolExec.execute(job);
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run()
			{
				corePoolExec.shutdown();
				scheduledPoolExec.shutdown();
			}
		});
	}
	
	public synchronized static PortalThreadService getInstance()
	{
		if(_instance == null)
		{
			_instance = new PortalThreadService();
		}
		return _instance;
	}

	public void execute(Runnable arg0) 
	{
		corePoolExec.execute(arg0);
	}

	public ThreadFactory getThreadFactory() {
		return corePoolExec.getThreadFactory();
	}

	public boolean isShutdown() {
		return corePoolExec.isShutdown();
	}

	public boolean isTerminated() {
		return corePoolExec.isTerminated();
	}

	public boolean isTerminating() {
		return corePoolExec.isTerminating();
	}

	public boolean remove(Runnable task) {
		return corePoolExec.remove(task);
	}

	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		return scheduledPoolExec.schedule(callable, delay, unit);
	}

	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return scheduledPoolExec.schedule(command, delay, unit);
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return scheduledPoolExec.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		return scheduledPoolExec.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}

	public void shutdown() {
		corePoolExec.shutdown();
		scheduledPoolExec.shutdown();
	}

	public List<Runnable> shutdownNow() 
	{
		List<Runnable> ret = new ArrayList<Runnable>();
		List<Runnable> tmp = corePoolExec.shutdownNow();
		if(tmp != null)
			ret.addAll(tmp);
		
		tmp = scheduledPoolExec.shutdownNow();
		if(tmp != null)
			ret.addAll(tmp);
		
		return ret;
	}

	public <T> Future<T> submit(Callable<T> task) {
		return corePoolExec.submit(task);
	}

	public <T> Future<T> submit(Runnable task, T result) {
		return corePoolExec.submit(task, result);
	}

	public Future<?> submit(Runnable task) {
		return corePoolExec.submit(task);
	}


	public int getMaximumPoolSize() {
		return corePoolExec.getMaximumPoolSize();
	}

	public int getPoolSize() {
		return corePoolExec.getPoolSize();
	}
	
	public int getCorePoolSize() {
		return corePoolExec.getCorePoolSize();
	}
	
	public int getActiveCount() {
		return corePoolExec.getActiveCount();
	}

	public long getCompletedTaskCount() {
		return corePoolExec.getCompletedTaskCount();
	}
	
	public int getScheduledMaximumPoolSize() {
		return scheduledPoolExec.getMaximumPoolSize();
	}

	public int getScheduledPoolSize() {
		return scheduledPoolExec.getPoolSize();
	}
	
	public int getScheduledCorePoolSize() {
		return scheduledPoolExec.getCorePoolSize();
	}
	
	public int getScheduledActiveCount() {
		return scheduledPoolExec.getActiveCount();
	}

	public long getScheduledCompletedTaskCount() {
		return scheduledPoolExec.getCompletedTaskCount();
	}	
	
	public void purge() {
		corePoolExec.purge();
		scheduledPoolExec.purge();
	}	
}
