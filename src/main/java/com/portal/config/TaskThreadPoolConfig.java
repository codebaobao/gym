package com.portal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="spring.task.pool")
@Component
public class TaskThreadPoolConfig {
	
	private int corePoolSize;
	
	private int corePoolMaxSize;
	
	private int threadTimeout;
	
	private int threadMonitorInterval;
	
	private int scheduledPoolSize;

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getCorePoolMaxSize() {
		return corePoolMaxSize;
	}

	public void setCorePoolMaxSize(int corePoolMaxSize) {
		this.corePoolMaxSize = corePoolMaxSize;
	}

	public int getThreadTimeout() {
		return threadTimeout;
	}

	public void setThreadTimeout(int threadTimeout) {
		this.threadTimeout = threadTimeout;
	}

	public int getThreadMonitorInterval() {
		return threadMonitorInterval;
	}

	public void setThreadMonitorInterval(int threadMonitorInterval) {
		this.threadMonitorInterval = threadMonitorInterval;
	}

	public int getScheduledPoolSize() {
		return scheduledPoolSize;
	}

	public void setScheduledPoolSize(int scheduledPoolSize) {
		this.scheduledPoolSize = scheduledPoolSize;
	}
	
}
