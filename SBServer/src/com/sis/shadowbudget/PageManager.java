/**
 * 
 */
package com.sis.shadowbudget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sis.shadowbudget.components.PageDescriptor;

/**
 * @author Alex
 *
 */
public class PageManager {
	private static PageManager _instance = null;
	private List<String> _pageList = new ArrayList<String>();
	private Map<String,PageDescriptor> _pageMap = new HashMap<String,PageDescriptor>(); 
	
	
	public static PageManager getInstance(){
		if(_instance == null){
			_instance = new PageManager();
		}
		
		return _instance;
	}
	
	private void addPage( PageDescriptor pDesc ) {
		_pageList.add(pDesc.getName());
		_pageMap.put(pDesc.getName(), pDesc);
	}
	
	public List<String> getPages(){
		return _pageList;
	}
	
	public PageDescriptor getPage(String name){
		return _pageMap.get(name);
	}
	
	static {
		PageManager.getInstance().addPage(new PageDescriptor("pmain", "Главная", null));
		PageManager.getInstance().addPage(new PageDescriptor("paccounts", "Счета", null));
		PageManager.getInstance().addPage(new PageDescriptor("ptransfers", "Проводки", null));
		PageManager.getInstance().addPage(new PageDescriptor("porgs", "Компании", null));
		PageManager.getInstance().addPage(new PageDescriptor("pusers", "Пользователи", null));
		PageManager.getInstance().addPage(new PageDescriptor("padmin", "Настройки", null));
	}
}
