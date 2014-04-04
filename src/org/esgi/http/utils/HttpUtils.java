package org.esgi.http.utils;

import org.esgi.http.handlers.HttpRequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: FuglyLionKing
 * Date: 04/04/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class HttpUtils {
    public static HttpRequestHandler parseRequest(InputStream stream, String remoteAdr) throws IOException {
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

    private static boolean isValidRequest(String request) {
        return null != request && !request.isEmpty();
    }
}
