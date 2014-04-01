package org.esgi.http.interfaces;

public interface ISession {
	Object getAttribute(String key);
	void setAttribute(String key, Object value);
	String getSessionId();
	String getCreationDate();
}
