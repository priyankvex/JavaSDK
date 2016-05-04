package io.cloudboost.beans;

public class CBResponse {
	String responseBody;
	String statusMessage;
	int statusCode;
	String sessionId;
	String error=null;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		String rtn="[Response-Body:"+getResponseBody()+",Status-Code:"+getStatusCode()+",Status-Message:"+getStatusMessage()+",Session-ID:"+getSessionId()+"]";
		return rtn;
	}

	public CBResponse(String responseBody, String statusMessage,
			int statusCode, String sessionId) {
		super();
		this.responseBody = responseBody;
		this.statusMessage = statusMessage;
		this.statusCode = statusCode;
		this.sessionId = sessionId;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
