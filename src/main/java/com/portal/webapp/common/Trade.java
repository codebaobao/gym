package com.portal.webapp.common;

public enum Trade {
	Body,
	Yoga,
	Swim,
	Wushu,
	Dance;
	
	private Trade()
	{
		
	}
	
	public static Trade getTrade(String value)
	{
		for(Trade trade : Trade.values())
		{
			if(trade.name().equals(value))
				return trade;
		}
		
		return Trade.Body;
	}
}
