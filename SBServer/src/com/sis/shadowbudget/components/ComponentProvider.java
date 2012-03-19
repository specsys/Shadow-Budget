/**
 * 
 */
package com.sis.shadowbudget.components;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;
import com.google.template.soy.tofu.SoyTofu.Renderer;
import com.sis.shadowbudget.ConfigManager;

/**
 * @author Alex
 *
 */
public class ComponentProvider {
	
	private static ComponentProvider _instance = null;
	
	private SoyFileSet.Builder _sfsBuilder = new SoyFileSet.Builder();
	private SoyTofu _tofu = null;
	
	private ComponentProvider(){
		this.init();
	}
	
	public static ComponentProvider getInstance() {
		if( _instance == null ){
			_instance = new ComponentProvider();
		}
		
		return _instance;
	}
	
	@SuppressWarnings("deprecation")
	private void init() {
		File folder = new File(ConfigManager.getInstance().getFilePath("soy"));
		for( File f : folder.listFiles() ) {
			_sfsBuilder.add(f);
		}
		_tofu = _sfsBuilder.build().compileToJavaObj();
	}
	
	public String getComponent( String name, Map<String,?> context ) {
		Renderer r = _tofu.newRenderer( String.format("components.%s.main",name));
		if(context != null){
			r.setData(context);
		}
		return r.render();
	}

}
