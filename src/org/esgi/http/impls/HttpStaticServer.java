package org.esgi.http.impls;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class HttpStaticServer {
    ServerSocket server = null;
    Socket currentConnexion;
    int port = 1234;


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

        try {
            System.out.println("En attente de connexion sur le port : " + server.getLocalPort());
            HashMap<String , String> hostList;
            while (true)
            {
                currentConnexion = server.accept();
                System.out.println("Nouvelle connexion : " + currentConnexion);


                hostList = new HashMap<String, String>();
                ObjectMapper objectMapper = new ObjectMapper();
                VirtualHost vh = objectMapper.readValue(new File("config.js"), VirtualHost.class);
                for(int i=0; i<vh.getHosts().length;i++)
                {
                    if((!vh.getHosts()[i].equals("}") || !vh.getHosts()[i].equals("}")) && vh.getHosts()[i].equals("name"))
                    {

                        hostList.put(vh.getHosts()[i+1], vh.getHosts()[i+3]);
                    }
                }


                try
                {
                    HeaderOnlyHttpRequestHandler header = getRequesHeader(currentConnexion.getInputStream(), currentConnexion.getRemoteSocketAddress().toString());
                    currentConnexion.getOutputStream().write(header.getUri().getBytes());

                    currentConnexion.getOutputStream().flush();
                }
                catch (IOException ex)
                { // end of connection.
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
