/**
 * 
 */
package com.sis.shadowbudget.components;

/**
 * @author Alex
 *
 */
public class PageDescriptor {
	
	private String _name;
	private String _title;
	private String _role;
	
	public PageDescriptor(String name, String title, String role){
		_name = name;
		_title = title;
		_role = role;
	}
	
	public String getName(){
		return _name;
	}
	
	public String getTitle(){
		return _title;
	}
	
	public String getRole(){
		return _role;
	}
	
	
}
