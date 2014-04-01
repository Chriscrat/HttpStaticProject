package org.esgi.http.handlers;

import org.esgi.http.enums.HTTP_METHOD;
import org.esgi.http.interfaces.ICookie;
import org.esgi.http.interfaces.IRequestHttpHandler;
import org.esgi.http.keepers.Cookie;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: FuglyLionKing
 * Date: 31/03/14
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class HttpRequestHandler implements IRequestHttpHandler {

    private String filesystemRoot;
    private HashMap<String, String> headers = new HashMap<String, String>();
    private HashMap<String, String> parameters = new HashMap<String, String>();
    private String uri;
    private HTTP_METHOD method;
    private String version;
    private String remoteAdr;

    private void parseUrl(String url) {
        String[] uriAndParam = url.split("\\?");
        uri = uriAndParam[0];

        if(2 > uriAndParam.length)
            return;

        for(String nameValueStr : uriAndParam[1].split("&")){
            String[] nameValue = nameValueStr.split("=");
            parameters.put(nameValue[0], nameValue.length != 1 ? nameValue[1] : "");
        }
    }

    private void setFilesystemRoot(String filesystemRoot){
        if(null == filesystemRoot)
            return;

        this.filesystemRoot = filesystemRoot.replaceAll("\\\\","/");
        if(filesystemRoot.endsWith("/"))
            this.filesystemRoot = filesystemRoot.substring(0,filesystemRoot.length()-1);
    }

    public HttpRequestHandler(String request, String remoteAdress, String filesystemRoot) {
        setFilesystemRoot(filesystemRoot);

        String[] parsed = request.split("\r\n");

        String[] headerHead = parsed[0].split(" ");

        method = HTTP_METHOD.valueOf(headerHead[0]);
        version = headerHead[2];

        parseUrl(headerHead[1]);

        remoteAdr = remoteAdress;

        for (int i = 1; i < parsed.length; ++i) {
            String[] things = parsed[i].split(": ");
            if (2 != things.length)
                continue;

            headers.put(things[0], things[1]);
        }
    }

    @Override
    public String[] getParameterNames() {
        return (String[])parameters.keySet().toArray();

    }

    @Override
    public String getParameter(String key) {
        return parameters.get(key);
    }

    private ICookie[] cookies = null;
    @Override
    public ICookie[] getCookies() {

        if(null != cookies)
            return cookies;

        String cookiesStr = headers.get("Cookie");

        if (null == cookiesStr)
            return new ICookie[0];

        String[] cookiesTab = cookiesStr.split("; ");
        ICookie[] iCookies = new ICookie[cookiesTab.length];

        for (int i = 0; i < cookiesTab.length; i++) {
            String[] keyVal = cookiesTab[i].split("=");
            iCookies[i] = new Cookie(keyVal[1], keyVal[0]);
        }

        return this.cookies = iCookies;
    }

    @Override
    public HTTP_METHOD getMethod() {
        return method;
    }

    @Override
    public String getHttpVersion() {
        return version;
    }

    @Override
    public String[] getHeaderNames() {
        return (String[]) headers.keySet().toArray();
    }

    @Override
    public String getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public String getRealPath(String path) {

        return String.format("%s%s%s",filesystemRoot, path.startsWith("/") ? "" : "/",path);
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
    public String getUrl() {
        return uri;
    }

    @Override
    public String getPort() {
        return null;
    }
}
