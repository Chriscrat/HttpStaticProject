package org.esgi.http.staticserver;

import org.esgi.http.handlers.HttpRequestHandler;
import org.esgi.http.handlers.ResponseHttpHandler;
import org.esgi.http.handlers.SimpleHttpHandler;
import org.esgi.http.impls.VirtualHost;
import org.esgi.http.interfaces.ICookie;
import org.esgi.http.interfaces.IRequestHttpHandler;
import org.esgi.http.interfaces.IResponseHttpHandler;
import org.esgi.http.interfaces.ISession;
import org.esgi.http.keepers.Session;
import org.esgi.http.keepers.SessionBank;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: FuglyLionKing
 * Date: 31/03/14
 * Time: 14:07
 * To change this template use File | Settings | File Templates.
 */
public class HttpStaticServer {
    ServerSocket server = null;
    Socket currentConnexion;
    int port = 1234;
    SimpleHttpHandler simpleHttpHandler;

    SessionBank sessions = new SessionBank(20000, 30000);


    public static void main(String[] str) {

        new HttpStaticServer().run();
    }

    public HttpStaticServer() {
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            System.err.println(String.format("Cannot open connexion on port %d : %s", port, ex));
        }

    }


    private boolean isValidRequest(String request) {
        return null != request && !request.isEmpty();
    }

    public HttpRequestHandler parseRequest(InputStream stream, String remoteAdr) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(stream);
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        String requestEnd = "\r\n\r\n";

        int b;
        while (0 < (b = reader.read(buffer))) {
            builder.append(buffer, 0, b);
            String bufferEnd = builder.substring(builder.length() - 4, builder.length());
            if (requestEnd.equals(bufferEnd))
                break;
        }


        return isValidRequest(builder.toString()) ? new HttpRequestHandler(builder.toString(), remoteAdr, null) : null;

    }

    private void countThisVisitsLookedPages(IResponseHttpHandler reponse, IRequestHttpHandler request) {
        if (null == reponse || null == request)
            return;

        String cookieName = "sessionId";
        String counterKey = "counter";
        ICookie[] cookies = request.getCookies();

        ISession session = null;

        for (ICookie cookie : cookies) {
            if (!cookieName.equals(cookie.getName()))
                continue;

            String sessionId = cookie.getValue();
            session = sessions.get(sessionId);
        }

        //no session cookie or session's expired
        if (null == session)
            session = new Session();

        Integer counter = (Integer) session.getAttribute(counterKey);
        counter = null == counter ? 1 : counter + 1;

        session.setAttribute(counterKey, counter);

        System.out.println(String.format("%s visited %d pages this session", session.getSessionId(), counter));

        reponse.addCookie(cookieName, session.getSessionId(), 60000, null);

        sessions.add(session);
    }

    private void handleConexion() throws IOException {
        HttpRequestHandler request = parseRequest(currentConnexion.getInputStream(), currentConnexion.getRemoteSocketAddress().toString());
        ResponseHttpHandler response = new ResponseHttpHandler(currentConnexion.getOutputStream());

        countThisVisitsLookedPages(response, request);

        simpleHttpHandler.execute(request, response);
    }

    public void run() {
        if (null == server)
            return;

        sessions.start();

        VirtualHost virtualHost = new VirtualHost();
        simpleHttpHandler = new SimpleHttpHandler(virtualHost.getHostList());


        System.out.println("Server awaiting connexion on  : " + server.getLocalPort());
        while (true) {
            try {

                currentConnexion = server.accept();
                System.out.println("New Connection : " + currentConnexion);

                handleConexion();

                System.out.println("Connexion closed");

            } catch (IOException ex) { // end of connection.
                System.err.println("Connexion terminated : " + ex);

            } finally {
                try {
                    if (null != currentConnexion)
                        currentConnexion.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

    }

}
