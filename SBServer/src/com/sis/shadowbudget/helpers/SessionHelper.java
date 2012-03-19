package com.sis.shadowbudget.helpers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sis.shadowbudget.security.NotAuthorizedException;

public class SessionHelper {
	@SuppressWarnings("unchecked")
	public static Object getUserAttribute(HttpServletRequest req, String attr) throws Exception{
		if(req.getSession().getAttribute("user") == null){
			throw new NotAuthorizedException();
		}

		return ((Map<String,Object>)req.getSession().getAttribute("user")).get(attr);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,?> getUserContext(HttpServletRequest req){
		return (Map<String,Object>)req.getSession().getAttribute("user");
	}

}
