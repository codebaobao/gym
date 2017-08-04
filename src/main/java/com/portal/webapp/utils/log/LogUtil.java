package com.portal.webapp.utils.log;

import org.apache.log4j.Level;

public class LogUtil 
{	
	public static ILogger getLogger(LogModule logModule, Class<?> clazz)
	{
		return new CoreLogger(logModule, clazz);
	}
	
	public static ILogger getLogger(LogModule logModule, String className)
	{
		return new CoreLogger(logModule, className);
	}
	
	public synchronized static void setLoggerLevel(Level level){
	}
	
}
