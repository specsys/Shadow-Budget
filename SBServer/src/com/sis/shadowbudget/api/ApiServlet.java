package com.sis.shadowbudget.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sis.shadowbudget.Module;
import com.sis.shadowbudget.ModuleManager;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class ApiServlet
 */
public abstract class ApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApiServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.handleHttp(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.handleHttp(request, response);
	}
	
	private void handleHttp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject respObj = new JSONObject();
		respObj.put("version", "1.0");
		try {
			JSONObject hObj = this.handle(request, response);
			respObj.put("response", hObj);
			respObj.put("status", true);
		} catch( Exception ex ){
			respObj.put("status", false);
			respObj.put("errorType", ex.getClass().getSimpleName());
			respObj.put("message", ex.getMessage());
		}
		
		response.getOutputStream().write(respObj.toString().getBytes("UTF-8"));
	}

	protected abstract JSONObject handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
