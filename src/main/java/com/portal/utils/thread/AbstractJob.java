package com.portal.utils.thread;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public abstract class AbstractJob implements Runnable
{
	private String jobName;
	private String originalName;
	private ScheduledFuture<?> scheduledTask;
	private List<IAsyncJobListener> jobListeners;
	
	public abstract void handle();
	
	public void run()
	{
		try{
			preHandle();
			handle();
			if(jobListeners != null)
			{
				for(IAsyncJobListener listener : jobListeners)
					listener.onExecuted();
			}
				
		}finally{
			postHandle();
		}
	}
	
	public void preHandle()
	{
		originalName = Thread.currentThread().getName();
		if(jobName != null) 
			Thread.currentThread().setName(jobName);
		else{
			if(!this.getClass().getSimpleName().isEmpty())
				Thread.currentThread().setName(this.getClass().getSimpleName());
		}
	}
	
	public void postHandle()
	{
		Thread.currentThread().setName(originalName);
	}
	
	public void execute()
	{		
		PortalThreadService thService = PortalThreadService.getInstance();
		thService.execute(this);
	}
	
	public void execute(long delay, TimeUnit unit)
	{		
		PortalThreadService thService = PortalThreadService.getInstance();
		scheduledTask = thService.schedule(this, delay, unit);
	}
	
	public void scheduleAtFixedRate(long initialDelay, long period, TimeUnit unit) {
		PortalThreadService thService = PortalThreadService.getInstance();
		scheduledTask = thService.scheduleAtFixedRate(this, initialDelay, period, unit);
	}
	
	public void stopScheduleTask()
	{
		if(scheduledTask != null)
			scheduledTask.cancel(true);
	}
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public void addJobListener(IAsyncJobListener jobListener) {
		if(jobListeners == null)
			jobListeners = new CopyOnWriteArrayList<IAsyncJobListener>();
		jobListeners.add(jobListener);
	}
	
}
