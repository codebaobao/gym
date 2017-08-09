package com.portal.utils.log;

public interface ILogger 
{
	public void info(String msg);
	
	public void error(String error);
	
	public void error(String error, Throwable ex);
	
	public void warn(String warning);
	
	public void warn(String warning, Throwable ex);
	
	public void debug(String msg);
	
	public boolean idDebugEnabled();
	
	
}
