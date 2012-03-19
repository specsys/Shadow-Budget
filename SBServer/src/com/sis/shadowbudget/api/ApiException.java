/**
 * 
 */
package com.sis.shadowbudget.api;

/**
 * @author Alex
 *
 */
public class ApiException extends Exception {
	private static final long serialVersionUID = -4862949281302870286L;

	public ApiException( String msg ) {
		super(msg);
	}
}
