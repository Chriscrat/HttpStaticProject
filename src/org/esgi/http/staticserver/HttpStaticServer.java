package org.esgi.http.staticserver;

import org.esgi.http.handlers.HttpRequestHandler;
import org.esgi.http.handlers.ResponseHttpHandler;
import org.esgi.http.handlers.SimpleHttpHandler;
import org.esgi.http.impls.VirtualHost;

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
        while (-1 != (b = reader.read(buffer))) {
            builder.append(buffer, 0, b);
            String bufferEnd = builder.substring(builder.length() - 4, builder.length());
            if (requestEnd.equals(bufferEnd))
                break;
        }


        return isValidRequest(builder.toString()) ? new HttpRequestHandler(builder.toString(), remoteAdr, null) : null;

    }


    public void run(){
        if (null == server)
            return;

        VirtualHost virtualHost = new VirtualHost();
        simpleHttpHandler = new SimpleHttpHandler(virtualHost.getHostList());


            System.out.println("Server awaiting connexion on  : " + server.getLocalPort());
            while (true) {
                try {
                    currentConnexion = server.accept();
                    System.out.println("New Connection : " + currentConnexion);

                    HttpRequestHandler requestHeader = parseRequest(currentConnexion.getInputStream(), currentConnexion.getRemoteSocketAddress().toString());
                    ResponseHttpHandler response = new ResponseHttpHandler(currentConnexion.getOutputStream());
                    simpleHttpHandler.execute(requestHeader, response);

                    currentConnexion.close();

                    System.out.println("Connexion closed");
                } catch (IOException ex) { // end of connection.
                    System.err.println("Connexion terminated : " + ex);
                }
            }

    }

}
