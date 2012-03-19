package com.sis.shadowbudget.components;

import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.sis.shadowbudget.ConfigManager;
import com.sis.shadowbudget.api.ApiServlet;
import com.sis.shadowbudget.api.InvalidAPIRequestException;
import com.sis.shadowbudget.helpers.SessionHelper;
import com.sis.shadowbudget.security.SecurityException;
import com.sis.shadowbudget.security.SecurityHelper;

/**
 * Servlet implementation class ComponentHostServlet
 */
public class ComponentHostServlet extends ApiServlet {
	private static final long serialVersionUID = 8202989521717243726L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public ComponentHostServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected JSONObject handle( HttpServletRequest request, HttpServletResponse response ) throws Exception {
		JSONObject respObj = new JSONObject();
		ConfigManager.getInstance().checkoutServletContext(this.getServletContext());
		String componentName = request.getParameter("component");
		if(componentName == null){
			throw new InvalidAPIRequestException("Parameter 'component' has not been specified");
		}
		
		if(!SecurityHelper.checkComponentPermission(request, componentName)){
			throw new SecurityException(String.format("Permission denied for obtaining component: '%s'", componentName));
		}
		String componentData = ComponentProvider.getInstance().getComponent(componentName,SessionHelper.getUserContext(request));
		respObj.put("data", componentData);
		return respObj;
	}

}
