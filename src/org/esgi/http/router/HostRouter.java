package org.esgi.http.router;

import org.esgi.http.interfaces.IResponseHttpHandler;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Voodoo
 * Date: 31/03/14
 * Time: 20:40
 * To change this template use File | Settings | File Templates.
 */
public class HostRouter {

    HashMap<String,String> hostToPath;

    public HostRouter(HashMap<String, String> hostToPath) {
        this.hostToPath = hostToPath;
    }

    public IResponseHttpHandler getResponse(String host, String uri, IResponseHttpHandler response){
        String root = hostToPath.get(host);

        ApacheLikeResponseFactory.fillResponse(root, uri, host, response);

        return response;
    }
}
