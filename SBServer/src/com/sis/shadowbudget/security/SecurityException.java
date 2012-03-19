/**
 * 
 */
package com.sis.shadowbudget.security;

import com.sis.shadowbudget.api.ApiException;

/**
 * @author Alex
 *
 */
public class SecurityException extends ApiException {
	private static final long serialVersionUID = 8688959849319385059L;

	public SecurityException(String msg) {
		super(msg);
	}

}
