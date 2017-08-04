package com.portal.webapp.utils.log;

public enum LogModule {
	Login,
	User,
    ;
	
	public static LogModule getModule(String s){
		LogModule[] values = LogModule.values();
		for (int i = 0; i < values.length; i++) {
			if(values[i].name().equalsIgnoreCase(s))
				return values[i];
		}
		return null;
	}
}
