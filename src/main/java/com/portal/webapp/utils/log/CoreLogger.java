package com.portal.webapp.utils.log;

import org.apache.log4j.Logger;

class CoreLogger implements ILogger
{
	private Logger logger;
	
	public CoreLogger(LogModule module, Class<?> clazz)
	{
		logger = Logger.getLogger(module.name() + "_" + clazz.getSimpleName());
	}
	
	public CoreLogger(LogModule module, String className)
	{
		logger = Logger.getLogger(module.name() + "_" + className);
	}
	
	@Override
	public void info(String msg) 
	{
		logger.info(msg);
	}

	@Override
	public void error(String error) 
	{
		logger.error(error);
	}

	@Override
	public void error(String error, Throwable ex) 
	{
		logger.error(error, ex);
	}

	@Override
	public void warn(String warning) 
	{
		logger.warn(warning);
	}

	@Override
	public void warn(String warning, Throwable ex) 
	{
		logger.warn(warning, ex);
	}

	@Override
	public void debug(String msg) {

		logger.debug(msg);
	}

	public boolean idDebugEnabled()
	{
		return logger.isDebugEnabled();
	}
	
}
