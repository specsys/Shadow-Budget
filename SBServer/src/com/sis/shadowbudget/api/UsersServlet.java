package com.sis.shadowbudget.api;

import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sis.shadowbudget.helpers.DatabaseHelper;

import net.sf.json.JSONObject;


/**
 * Servlet implementation class UsersServlet
 */
public class UsersServlet extends ApiServlet {
	private static final long serialVersionUID = 1334702251679510573L;

	/**
     * @see ApiServlet#ApiServlet()
     */
    public UsersServlet() {
        super();
    }

	@Override
	protected JSONObject handle( HttpServletRequest request, HttpServletResponse response ) throws Exception {
		String cmd = request.getParameter("cmd");
		if(cmd == null){
			throw new InvalidAPIRequestException("Parameter 'cmd' has not been specified");
		}
		
		if(cmd.compareTo("add") == 0){
			return handleAdd(request);
		} else if(cmd.compareTo("delete") == 0){
			return handleDelete(request);
		}

		throw new InvalidAPIRequestException(String.format("Unknown command: '%s'",cmd));
	}

	private JSONObject handleDelete(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	private JSONObject handleAdd(HttpServletRequest request) throws Exception {
		JSONObject resObj = new JSONObject();
		
		String userName = request.getParameter("user");
		if(userName == null){
			throw new InvalidAPIRequestException("Parameter 'user' has not been specified");
		}

		String pwd = request.getParameter("pwd");
		if(pwd == null){
			throw new InvalidAPIRequestException("Parameter 'pwd' has not been specified");
		}

		String firstName = request.getParameter("fname");
		String lastName = request.getParameter("lname");
		String email = request.getParameter("email");

		// validate data
		if(userName.length() == 0){
			throw new AddUserException("Username cannot be empty");
		}
		
		UUID uuid = DatabaseHelper.addUser(userName, pwd, firstName, lastName, email);
		resObj.put("uid", uuid);
		
		return resObj;
	}

}
