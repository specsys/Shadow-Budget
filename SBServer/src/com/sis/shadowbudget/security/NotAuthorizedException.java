/**
 * 
 */
package com.sis.shadowbudget.security;

import com.sis.shadowbudget.api.ApiException;

/**
 * @author Alex
 *
 */
public class NotAuthorizedException extends ApiException {
	private static final long serialVersionUID = -4778680018769812162L;

	public NotAuthorizedException() {
		super("Not authorized");
	}

}
