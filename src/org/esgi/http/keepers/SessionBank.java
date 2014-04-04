package org.esgi.http.keepers;

import org.esgi.http.interfaces.ISession;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: Voodoo
 * Date: 01/04/14
 * Time: 20:32
 * To change this template use File | Settings | File Templates.
 */
public class SessionBank extends Thread{

    private Map<String, ISession> sessions = Collections.synchronizedMap(new HashMap<String, ISession>());
    private long maxAge = 36000;
    private long cleaningInterval;
    private AtomicBoolean run = new AtomicBoolean(true);

    public SessionBank(long maxAge, long cleaningInterval) {
        this.maxAge = maxAge;
        this.cleaningInterval = cleaningInterval;
    }


    @Override
    public void run() {
        while (run.get()){
           // System.out.println("Cleaning !");
            clean();
            //System.out.println("Going to sleep");

            try {
                sleep(cleaningInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void thisIsTheEndMyFriend(){
        run.lazySet(false);
    }

    public void add(ISession session){
        sessions.put(session.getSessionId(), session);
    }

    public ISession get(String id){
        ISession session = null;
        synchronized (sessions){
            session = sessions.get(id);
        }
        return session;
    }

//    public ISession remove(String id){
//        ISession session = null;
//        synchronized (sessions){
//            session = sessions.remove(id);
//        }
//        return session;
//    }

    private void clean(){
        long now = System.currentTimeMillis();

        ArrayList<String> pendingRemoves = new ArrayList<String>();

        for(String key : sessions.keySet()){
            ISession session = sessions.get(key);
            if(null == session || session.getCreationDate().getTime() + maxAge < now)
                pendingRemoves.add(key);
        }

        for(String id : pendingRemoves)
            sessions.remove(id);

    }


}
