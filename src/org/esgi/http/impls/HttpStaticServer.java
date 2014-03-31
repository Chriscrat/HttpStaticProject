package org.esgi.http.impls;

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
    int port = 80;
    SimpleHttpHandler simpleHttpHandler;


    public static void main(String[] str) {
        new HttpStaticServer().run();
    }

    public HttpStaticServer() {
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            System.err.println("Impossible de créer un socket serveur sur ce port : " + ex);

            try { // trying an anonymous one.
                server = new ServerSocket(0);
            } catch (IOException ex2) { // Impossible to connect!
                System.err.println("Impossible de créer un socket serveur : " + ex2);
            }
        }

    }



    public HeaderOnlyHttpRequestHandler getRequesHeader(InputStream stream, String remoteAdr){
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(stream);
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        String requestEnd = "\r\n\r\n";

        try {
            int b;
            while (-1 != (b = reader.read(buffer))){
                builder.append(buffer, 0, b);
                String bufferEnd = builder.substring(builder.length()-4, builder.length());
                if(requestEnd.equals(bufferEnd))
                    break;
            }

            System.out.println("Fin de connexion");
        }
        catch (IOException ex) { // end of connection.
            System.err.println("Fin de connexion : "+ex);
        }

        System.out.println(builder.toString());

        return new HeaderOnlyHttpRequestHandler(builder.toString(), remoteAdr);

    }

    public void run() {
        if (null == server)
            return;

        //TODO parse config.js and get feed this the hashmap
        simpleHttpHandler = new SimpleHttpHandler(null);

        try {
            System.out.println("En attente de connexion sur le port : " + server.getLocalPort());
            while (true) {
                currentConnexion = server.accept();
                System.out.println("Nouvelle connexion : " + currentConnexion);
                try {

                    HeaderOnlyHttpRequestHandler requestHeader = getRequesHeader(currentConnexion.getInputStream(), currentConnexion.getRemoteSocketAddress().toString());
                    SimpleResponseHttHandler response = new SimpleResponseHttHandler(currentConnexion.getOutputStream());
                    simpleHttpHandler.execute(requestHeader,response);

                } catch (IOException ex) { // end of connection.
                    System.err.println("Fin de connexion : " + ex);
                }
                currentConnexion.close();
            }
        } catch (Exception ex) {
            // Error of connection
            System.err.println("Une erreur est survenue : " + ex);
            ex.printStackTrace();
        }
    }

}
