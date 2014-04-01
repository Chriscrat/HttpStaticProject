package org.esgi.http.impls;

import org.esgi.http.interfaces.IResponseHttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created with IntelliJ IDEA.
 * User: Voodoo
 * Date: 31/03/14
 * Time: 21:56
 * To change this template use File | Settings | File Templates.
 */
public class SimpleResponseHttHandler implements IResponseHttpHandler{
    OutputStreamWriter writer;
    OutputStream stream;

    public SimpleResponseHttHandler(OutputStream stream) {
        this.writer = new OutputStreamWriter(this.stream = stream);
    }

    @Override
    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Writer getWriter() {
        return writer;
    }

    @Override
    public OutputStream getOutputStream() {
        return stream;
    }

    @Override
    public void addHeader(String key, String value) {
    }

    @Override
    public void setContentType(String contentType) {
    }

    @Override
    public void addCookie(String name, String value, int duration, int path) {
    }
}
