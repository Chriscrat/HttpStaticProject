package org.esgi.http.interfaces;

import java.io.OutputStream;
import java.io.Writer;

public interface IResponseHttpHandler {
	
	void flush();
	Writer getWriter();
	OutputStream getOutputStream();
	void addHeader(String key, String value);
	void setContentType(String contentType);
	void addCookie(String name, String value, int duration, String path);
	void setHttpCode(String code);
	void setErrorCode();
	void setContentLength(int lenght);

}
