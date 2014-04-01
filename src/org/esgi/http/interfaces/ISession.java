package org.esgi.http.interfaces;

import java.util.Date;

public interface ISession {
	Object getAttribute(String key);
	void setAttribute(String key, Object value);
	String getSessionId();
	Date getCreationDate();
}
