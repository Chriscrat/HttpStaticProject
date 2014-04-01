package org.esgi.http.impls;

import org.esgi.http.interfaces.ICookie;
import org.esgi.http.interfaces.IRequestHttpHandler;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: FuglyLionKing
 * Date: 31/03/14
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class HeaderOnlyHttpRequestHandler implements IRequestHttpHandler {

    private HashMap<String, String> map = new HashMap<String, String>();
    private String uri;
    private String method;
    private String version;
    private String remoteAdr;



    public HeaderOnlyHttpRequestHandler(String request, String remoteAdress) {
        String[] parsed = request.split("\r\n");

        String[] headerHead = parsed[0].split(" ");

       method = headerHead[0];
       uri = headerHead[1];
       version = headerHead[2];

       remoteAdr = remoteAdress;

        for(int i = 1; i < parsed.length; ++i){
            String[] things = parsed[i].split(": ");
            if(2 != things.length)
                continue;

            map.put(things[0], things[1]);
        }
    }

    @Override
    public String[] getParameterNames() {
        return new String[0];

    }

    @Override
    public String getParameter(String key) {
        return null;
    }

    @Override
    public ICookie[] getCookies() {
        String cookiesStr = map.get("Cookie");

        if(null == cookiesStr)
            return new ICookie[0];

        String[] cookies = cookiesStr.split("; ");
        ICookie[] cookiesTabs = new ICookie[cookies.length];

        for(int i = 0; i < cookies.length; i++){
            String[] keyVal = cookies[i].split("=");
            cookiesTabs[i]=new Cookie(keyVal[0],keyVal[1]);
        }

        return cookiesTabs;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getHttpVersion() {
        return version;
    }

    @Override
    public String[] getHeaderNames() {
       return (String[]) map.keySet().toArray();
    }

    @Override
    public String getHeader(String key) {
       return map.get(key);
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public String getHostname() {
        return null;
    }

    @Override
    public String getRemoteAddress() {
        return remoteAdr;
    }

    @Override
    public String getUrl(){
        return uri;
    }

    @Override
    public String getPort() {
        return null;
    }
}
