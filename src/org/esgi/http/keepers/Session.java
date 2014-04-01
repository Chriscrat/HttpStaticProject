package org.esgi.http.keepers;

import org.esgi.http.interfaces.ISession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Voodoo
 * Date: 01/04/14
 * Time: 20:23
 * To change this template use File | Settings | File Templates.
 */
public class Session implements ISession{

    HashMap<String, Object> session = new HashMap<String, Object>();
    Date creationDate;
    String sessionId;

    public Session() {
        creationDate = new Date();
        sessionId = String.valueOf(System.nanoTime());
    }

    @Override
    public Object getAttribute(String key) {
        return session.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        session.put(key,value);
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }
}
