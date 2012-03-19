/**
 * 
 */
package com.sis.shadowbudget;

/**
 * @author Alex
 *
 */
public class ModuleManager {

	private static ModuleManager _instance = null;
	
	private ModuleManager() {
		this.scanAndLoadModules();
	}
	
	public static ModuleManager getInstance() {
		if(_instance == null){
			_instance = new ModuleManager();
		}
		
		return _instance;
	}
	
	private void scanAndLoadModules() {
		
	}
	
	public Module getModule( String moduleName ) {
		return null;
	}
}
