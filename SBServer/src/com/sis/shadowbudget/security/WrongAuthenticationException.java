/**
 * 
 */
package com.sis.shadowbudget.security;

import com.sis.shadowbudget.api.ApiException;

/**
 * @author Alex
 *
 */
public class WrongAuthenticationException extends ApiException {
	private static final long serialVersionUID = -6440055756871038306L;

	public WrongAuthenticationException() {
		super("Wrong authenticatoin credits specified");
	}
}
