/**
 * 
 */
package com.sis.shadowbudget;

import javax.servlet.ServletContext;

/**
 * @author Alex
 *
 */
public class ConfigManager {
	private static ConfigManager _instance = null;
	private ServletContext _ctx = null;
	
	private ConfigManager(){
		
	}
	
	public static ConfigManager getInstance() {
		if( _instance == null ) {
			_instance = new ConfigManager();
		}
		
		return _instance;
	}
	
	public void checkoutServletContext( ServletContext ctx ) {
		_ctx = ctx;
	}
	
	public String getFilePath( String relPath ) {
		if( _ctx == null ) {
			throw new RuntimeException( "Servlet context has not been set" );
		}
		
		return _ctx.getRealPath(relPath);
	}
}
