package com.portal.webapp.common;

public enum Role {
	Administrator,
	Personal,
	Organ;
	
	private Role()
	{
		
	}
	
	public static Role getRole(String value)
	{
		for(Role role : Role.values())
		{
			if(role.name().equals(value))
				return role;
		}
		
		return Role.Personal;
	}
}
