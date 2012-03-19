package com.sis.shadowbudget.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.sis.shadowbudget.PageManager;
import com.sis.shadowbudget.api.ApiServlet;
import com.sis.shadowbudget.api.InvalidAPIRequestException;
import com.sis.shadowbudget.components.PageDescriptor;
import com.sis.shadowbudget.helpers.DatabaseHelper;

import net.sf.json.JSONObject;


/**
 * Servlet implementation class AuthServlet
 */
public class AuthServlet extends ApiServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthServlet() {
        super();
    }

	@Override
	protected JSONObject handle(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String cmd = request.getParameter("cmd");
		if(cmd == null){
			throw new InvalidAPIRequestException("Parameter 'cmd' has not been specified");
		}
		if(cmd.compareTo("login") == 0){
			return handleLogin(request);
		} else if(cmd.compareTo("logout") == 0){
			return handleLogout(request);
		}
		
		throw new InvalidAPIRequestException(String.format("Unknown command: '%s'",cmd));
		
	}

	private JSONObject handleLogout(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private JSONObject handleLogin(HttpServletRequest request) throws Exception {
		JSONObject respObj = new JSONObject();

		String userName = request.getParameter("user");
		if(userName == null){
			throw new InvalidAPIRequestException("Parameter 'user' has not been specified");
		}

		String pwd = request.getParameter("pwd");
		if(pwd == null){
			throw new InvalidAPIRequestException("Parameter 'pwd' has not been specified");
		}
		
		if(userName.length() == 0){
			throw new WrongAuthenticationException();
		}
		

		Map<String,Object> userMap = DatabaseHelper.getUser(userName, SecurityHelper.getHash(userName, pwd));
		if(userMap == null){
			throw new WrongAuthenticationException();
		}
		request.getSession().setAttribute("user", userMap);
//		ComponentsPermission cp = SecurityHelper.generateComponentPermissions(userMap);
		ComponentsPermission cp = new ComponentsPermission();
		userMap.put("permissions", cp);
		
		int userId = Integer.parseInt(userMap.get("id").toString());
		Set<String> roles = DatabaseHelper.getUserRoles(userId);

		List<String> pages = new ArrayList<String>();
		Map<String,String> titles = new HashMap<String,String>(); 
		for( String pName : PageManager.getInstance().getPages()){
			PageDescriptor pd = PageManager.getInstance().getPage(pName);
			if(pd.getRole() == null || roles.contains(pd.getRole())){
				// true
				pages.add(pName);
				titles.put(pName, pd.getTitle());
				cp.put(pName, true);
			} else {
				// false
				cp.put(pName, false);
			}
		}
		userMap.put("pages", pages);
		userMap.put("pageTitles", titles);

		respObj.put("user", userMap.get("user"));
		respObj.put("firstName", userMap.get("firstName"));
		respObj.put("lastName", userMap.get("lastName"));
		respObj.put("email", userMap.get("email"));
		respObj.put("uid", userMap.get("uid"));
		
		return respObj;
	}

}
