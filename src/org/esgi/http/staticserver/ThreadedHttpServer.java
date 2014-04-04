package org.esgi.http.staticserver;

import org.esgi.http.handlers.SimpleHttpHandler;
import org.esgi.http.impls.VirtualHost;
import org.esgi.http.thread.ConnectionWork;
import org.esgi.http.thread.FixedThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: FuglyLionKing
 * Date: 04/04/14
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
public class ThreadedHttpServer {
    private FixedThreadPool pool;

    ServerSocket server = null;
    Socket currentConnexion;
    int port;

    public ThreadedHttpServer(int port, int poolSize) {
        pool = new FixedThreadPool(poolSize);

        this.port = port;
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            System.err.println(String.format("Cannot open connexion on port %d : %s", port, ex));
        }
    }

    public void run() {
        if (null == server)
            return;

        VirtualHost virtualHost = new VirtualHost();


        System.out.println("Server awaiting connexion on  : " + server.getLocalPort());
        while (true) {
            try {

                currentConnexion = server.accept();
                System.out.println("New Connection : " + currentConnexion);
                pool.addJob(new ConnectionWork(currentConnexion, new SimpleHttpHandler(virtualHost.getHostList())));
                System.out.println("Connexion closed");

            } catch (IOException ex) {
                System.err.println("Connexion terminated : " + ex);
            }

        }
    }


}
