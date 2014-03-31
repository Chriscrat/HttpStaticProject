package org.esgi.http.impls;

import org.esgi.http.interfaces.IHttpHandler;
import org.esgi.http.interfaces.IRequestHttpHandler;
import org.esgi.http.interfaces.IResponseHttpHandler;
import org.esgi.http.router.HostRouter;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Voodoo
 * Date: 31/03/14
 * Time: 21:51
 * To change this template use File | Settings | File Templates.
 */
public class SimpleHttpHandler implements IHttpHandler{
    HostRouter hostRouter;

    public SimpleHttpHandler(HashMap<String, String> hostToPath) {
        hostRouter = new HostRouter(hostToPath);
    }

    @Override
    public void execute(IRequestHttpHandler request, IResponseHttpHandler response) throws IOException
    {
        hostRouter.getResponse(request.getHeader("Host"), request.getUri(),response).flush();
    }
}
