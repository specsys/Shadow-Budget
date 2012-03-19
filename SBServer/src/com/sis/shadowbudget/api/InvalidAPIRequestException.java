/**
 * 
 */
package com.sis.shadowbudget.api;

/**
 * @author Alex
 *
 */
public class InvalidAPIRequestException extends ApiException {

	private static final long serialVersionUID = -1248517755135892085L;

	public InvalidAPIRequestException(String msg) {
		super(msg);
	}

}
