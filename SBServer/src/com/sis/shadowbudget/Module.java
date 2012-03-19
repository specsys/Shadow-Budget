/**
 * 
 */
package com.sis.shadowbudget;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

/**
 * @author Alex
 *
 */
public interface Module {
	JSONObject handle( HttpServletRequest request, Map<String,String> params );
}
