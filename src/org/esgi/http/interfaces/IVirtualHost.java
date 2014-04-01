package org.esgi.http.interfaces;

public interface IVirtualHost
{
    String[] getHosts();
    void setHost(int pos, String host);
    String getName();
    void setName(String name);
    String getDocumentroot();
    void setDocumentRoot(String name);
}
