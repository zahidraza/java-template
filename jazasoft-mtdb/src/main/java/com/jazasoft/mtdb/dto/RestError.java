package com.jazasoft.mtdb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestError {
	public static final int BAD_REQUEST_SIMPLE = 40000;
	public static final int BAD_REQUEST_BEAN_SINGLE = 40001;
	public static final int BAD_REQUEST_BEAN_MULTIPLE = 40002;

	private int status;
	private int code;
	/**
	 * Non Field Error. Mainly Runtime Errors
	 */
	private String message;

	/**
	 * Field Error: Field errors if only one bean
	 */
	private Collection<Error> errors;

	/**
	 * Field Error: Field Error if there are multiple beans
	 */
	private Map<String, Collection<Error>> errorMap;

	private String devMessage;
	private String moreInfo;

	public RestError(int status, int code, String message) {
		this(status,code,message,"","");
	}

	public RestError(int status, int code, Map<String, Collection<Error>> errorMap) {
		this.status = status;
		this.code = code;
		this.errorMap = errorMap;
	}

	public RestError(int status, int code, Collection<Error> errors) {
		this.status = status;
		this.code = code;
		this.errors = errors;
	}

	public RestError(int status, int code, String message, String devMessage, String moreInfo) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
		this.devMessage = devMessage;
		this.moreInfo = moreInfo;
	}
}
