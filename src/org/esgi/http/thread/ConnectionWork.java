package org.esgi.http.thread;

import org.esgi.http.handlers.HttpRequestHandler;
import org.esgi.http.handlers.ResponseHttpHandler;
import org.esgi.http.interfaces.IHttpHandler;
import org.esgi.http.utils.HttpUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: FuglyLionKing
 * Date: 04/04/14
 * Time: 15:03
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionWork implements Comparable<ConnectionWork>, Runnable {

    Socket currentConnexion;
    IHttpHandler handler;

    public ConnectionWork(Socket connexion, IHttpHandler handler) {
        this.currentConnexion = connexion;
        this.handler = handler;
    }

    @Override
    public int compareTo(ConnectionWork o) {
        return 0;
    }

    @Override
    public void run() {
        try {

            handleConexion();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != currentConnexion)
                try {
                    currentConnexion.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void handleConexion() throws IOException {
        System.out.println(String.format("Connexion handled from %s", Thread.currentThread().getName()));

        HttpRequestHandler request = HttpUtils.parseRequest(currentConnexion.getInputStream(), currentConnexion.getRemoteSocketAddress().toString());
        ResponseHttpHandler response = new ResponseHttpHandler(currentConnexion.getOutputStream());

        handler.execute(request, response);
    }
}
