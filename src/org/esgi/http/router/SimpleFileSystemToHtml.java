package org.esgi.http.router;

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
public class SimpleFileSystemToHtml
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

    public static void fillResponse(String root, String uri, String hostname, IResponseHttpHandler response){
        if(null == root){
            make404(response);
            return;
        }

        root = System.getProperty("user.dir")+root;
        File file = new File(root+uri);
        System.out.println(file.getAbsolutePath());

        if(!file.exists()){
            make404(response);
            return;
        }

        if(file.isDirectory())
        {
            String htmlPage = directoryAsHtml(root,uri,hostname);

            response.setHttpCode("200");
            response.setContentLength(htmlPage.getBytes().length);
            response.setContentType("text/html");

            try {

                response.getWriter().append(htmlPage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                response.setHttpCode("200");
                byte[] content = Files.readAllBytes(file.toPath());
                response.setContentLength(content.length);
                response.setContentType(Files.probeContentType(file.toPath()));

                response.getOutputStream().write(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
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


    private static String directoryAsHtml(String root, String uri, String hostname)
    {
        File directory = new File(root+uri);
        StringBuilder response = new StringBuilder();
        response.append(String.format(PRE_LIST,uri,uri));
        for(File f : directory.listFiles())
        {
            String href = uri+ (uri.endsWith("/") ? "" : "/" )+f.getName();
            response.append(String.format(LIST_ELEM,href,f.getName()));
        }
        response.append(String.format(POST_LIST,new Date().toString(),hostname));

        return response.toString();
    }

}



/*
<html><head>
<title>Index of ???uri???</title>
</head>
<body>
<h1>Index of ???uri???</h1>
<pre><img src="/icons/blank.gif" alt="     "> <a href="?N=D">Name</a>                    <a href="?M=A">Last modified</a>       <a href="?S=A">Size</a>  <a href="?D=A">Description</a>
<hr>
<a href="/EPUB%20Collection/Priest,Christopher-Le%20monde%20inverti(The%20Inverted%20World)(1974).French.ebook.AlexandriZ/">Parent Directory</a>        02-Jul-2013 22:25      -
<a href="Priest,Christopher-Le%20monde%20inverti(The%20Inverted%20World)(1974).(HTML).French.ebook.AlexandriZ.zip">Priest,Christopher-L..&gt;</a> 02-Jul-2013 22:26   208k
<a href="Priest,Christopher-Le%20monde%20inverti(The%20Inverted%20World)(1974).Cover.French.ebook.AlexandriZ.jpg">Priest,Christopher-L..&gt;</a> 02-Jul-2013 22:26    78k
<a href="Priest,Christopher-Le%20monde%20inverti(The%20Inverted%20World)(1974).French.ebook.AlexandriZ.doc">Priest,Christopher-L..&gt;</a> 02-Jul-2013 22:26   827k
<a href="Priest,Christopher-Le%20monde%20inverti(The%20Inverted%20World)(1974).French.ebook.AlexandriZ.epub">Priest,Christopher-L..&gt;</a> 02-Jul-2013 22:26   345k
<a href="Priest,Christopher-Le%20monde%20inverti(The%20Inverted%20World)(1974).French.ebook.AlexandriZ.mobi">Priest,Christopher-L..&gt;</a> 02-Jul-2013 22:26   450k
<a href="Priest,Christopher-Le%20monde%20inverti(The%20Inverted%20World)(1974).French.ebook.AlexandriZ.nfo">Priest,Christopher-L..&gt;</a> 02-Jul-2013 22:26    12k
<a href="Priest,Christopher-Le%20monde%20inverti(The%20Inverted%20World)(1974).OCR.French.ebook.AlexandriZ.pdf">Priest,Christopher-L..&gt;</a> 02-Jul-2013 22:26   1.4M
<hr>
<address>BackScratch ???Date??? Server at ???hostname???</address>

</body></html>
 */
