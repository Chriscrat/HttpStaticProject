package org.esgi.http.staticserver;

/**
 * Created with IntelliJ IDEA.
 * User: FuglyLionKing
 * Date: 04/04/14
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
public class HttpServerStarter {

    public static void main(String[] str) {

//        new HttpStaticServer(1234).run();
        new ThreadedHttpServer(1234,5).run();
    }
}
