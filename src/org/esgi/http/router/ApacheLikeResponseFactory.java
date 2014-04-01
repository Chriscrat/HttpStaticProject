package org.esgi.http.router;

import org.esgi.http.enums.HTTP_CODES;
import org.esgi.http.interfaces.IResponseHttpHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Voodoo
 * Date: 31/03/14
 * Time: 20:46
 * To change this template use File | Settings | File Templates.
 */
public class ApacheLikeResponseFactory
{

    private static String fourOfour = "<body>404</body>";

    public static IResponseHttpHandler make404(IResponseHttpHandler response){
        response.setErrorCode();
        response.setContentLength(fourOfour.getBytes().length);
        response.setContentType("text/html");

        try {

            response.getWriter().append(fourOfour);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static void makeFileDownloader(File file, IResponseHttpHandler response){
        try {
            response.setHttpCode(HTTP_CODES.OK);
            byte[] content = Files.readAllBytes(file.toPath());

            response.setContentLength(content.length);
            response.setContentType(Files.probeContentType(file.toPath()));

            response.addHeader("Content-Description", "File Transfer");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s",file.getName()));


            //Hack to force headers writing
            response.getWriter().write(new char[0]);
            //flush stream so the header's sent, otherwise it will be push after the direct call to the stream's write method
            response.flush();

            response.getOutputStream().write(content);
        } catch
                (IOException e) {
            e.printStackTrace();
        }

    }
    public static void makeDirectoryExplorer(String root, String uri, String hostname, IResponseHttpHandler response){
        String htmlPage = directoryAsHtml(root,uri,hostname);

        response.setHttpCode(HTTP_CODES.OK);
        response.setContentLength(htmlPage.getBytes().length);
        response.setContentType("text/html");

        try {

            response.getWriter().append(htmlPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fillResponse(String root, String uri, String hostname, IResponseHttpHandler response){
        if(null == root){
            make404(response);
            return;
        }

        root = System.getProperty("user.dir")+root;
        File file = new File(root+uri);

        if(!file.exists()){
            make404(response);
            return;
        }

        if(file.isDirectory())
        {
            makeDirectoryExplorer(root,uri,hostname,response);
        }
        else
        {
            makeFileDownloader(file,response);
        }
    }

    //uri, uri
    private static String PRE_LIST = "<html><head>\n" +
            "<title>Index of %s</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>Index of %s</h1>\n" +
            "<hr>";
    //uri, name
    private static String LIST_ELEM = "<a href=\"%s\">%s</a><br>";
    //date, hostname
    private static String POST_LIST = "</hr><address>BackScratch %s Server at %s</address>\n" +
            "</body></html>";

    private static String getParent(String uri){
        if(uri.isEmpty() || uri.equals("/"))
            return null;
        int lastSlash = 0, i = 0;
        //We don't want the last slash if it is at the end of uri
        while(i < uri.length()-1)
            if('/' == uri.charAt(i++))
                lastSlash = i;

        return 0 == lastSlash ? "/" : uri.substring(0,lastSlash);
    }

    private static String directoryAsHtml(String root, String uri, String hostname)
    {
        File directory = new File(root+uri);
        StringBuilder response = new StringBuilder();
        response.append(String.format(PRE_LIST,uri,uri));
        //if we're not in root directory, add a parent directory link

        String parent = getParent(uri);
        if(null != parent){
            response.append(String.format(LIST_ELEM,parent,".."));
        }


        for(File f : directory.listFiles())
        {
            String href = uri+ (uri.endsWith("/") ? "" : "/" )+f.getName();
            response.append(String.format(LIST_ELEM,href,f.getName()));
        }
        response.append(String.format(POST_LIST,new Date().toString(),hostname));

        return response.toString();
    }

}

