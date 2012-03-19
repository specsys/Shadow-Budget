package com.sis.shadowbudget.security;

import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

import com.sis.shadowbudget.helpers.DatabaseHelper;
import com.sis.shadowbudget.helpers.SessionHelper;

public class SecurityHelper {
	public static String getHash( String userName, String password) throws Exception{
		// calculate MD5
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(userName.getBytes("UTF-8"));
		md.update(password.getBytes("UTF-8"));
		byte[] hashBytes = md.digest();
		Base64 b64Encoder = new Base64();
		return b64Encoder.encodeToString(hashBytes);
	}
	
	public static boolean checkComponentPermission(HttpServletRequest req, String componentName) throws Exception{
		if(componentName.compareToIgnoreCase("auth") == 0){
			return true;
		}
		if(Integer.parseInt(SessionHelper.getUserAttribute(req, "su").toString()) == 1 ){
			return true;
		}
		ComponentsPermission cp = (ComponentsPermission)SessionHelper.getUserAttribute(req, "permissions");
		if(cp.containsKey(componentName) && !cp.get(componentName).booleanValue()){
			return false;
		}
		return true;
	}
	
	public static ComponentsPermission generateComponentPermissions(Map<String,Object> userMap) throws Exception{
		ComponentsPermission cp = new ComponentsPermission();
		int userId = Integer.parseInt(userMap.get("id").toString());
		Set<String> roles = DatabaseHelper.getUserRoles(userId);
		boolean isAdmin = roles.contains("admin") || Integer.parseInt(userMap.get("su").toString()) == 1;
		// add components
		cp.put("admin", isAdmin);
		
		return cp;
	}
}
