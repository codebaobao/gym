package com.portal.utils.thread;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.config.TaskThreadPoolConfig;
import com.portal.utils.log.ILogger;
import com.portal.utils.log.LogModule;
import com.portal.utils.log.LogUtil;

@Component
class PortalMonitorJob extends AbstractJob
{
	private static final ILogger logger = LogUtil.getLogger(LogModule.Thread, PortalMonitorJob.class);
	
	@Autowired
	private TaskThreadPoolConfig taskThreadPoolConfig;
	
	private boolean stopJob;
	
	@Override
	public void handle() 
	{
//		int interval = taskThreadPoolConfig.getThreadMonitorInterval();
		int interval = 100;
		PortalThreadService threadService = PortalThreadService.getInstance();
		while(!stopJob)
		{
			logger.info("Core_Thread_Pool:completed = " + threadService.getCompletedTaskCount() + ",  active = " + threadService.getActiveCount() 
						+ ", PoolSize=" + threadService.getPoolSize() + ", CorePoolSize=" + threadService.getCorePoolSize() 
						+ ", MaximumPoolSize=" + threadService.getMaximumPoolSize());
			
			logger.info("Schd_Thread_Pool:completed = " + threadService.getScheduledCompletedTaskCount() + ",  active = " + threadService.getScheduledActiveCount() 
					+ ", PoolSize=" + threadService.getScheduledPoolSize() + ", CorePoolSize=" + threadService.getScheduledCorePoolSize());
				//	+ ", MaximumPoolSize=" + threadService.getScheduledMaximumPoolSize());
			try{
				TimeUnit.SECONDS.sleep(interval);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

	public boolean isStopJob() {
		return stopJob;
	}

	public void setStopJob(boolean stopJob) {
		this.stopJob = stopJob;
	}
	
}
