package org.esgi.http.impls;

import org.esgi.http.interfaces.IVirtualHost;

public class VirtualHost implements IVirtualHost
{
    private String[] hosts;
    private String name;
    private String documentroot;

    public VirtualHost()
    {

    }

    @Override
    public String[] getHosts()
    {
        return this.hosts;
    }

    @Override
    public void setHost(int pos, String host)
    {
        hosts[pos]=host;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public void setName(String name)
    {
        this.name=name;
    }

    @Override
    public String getDocumentroot()
    {
        return this.documentroot;
    }

    @Override
    public void setDocumentRoot(String documentroot)
    {
        this.documentroot=documentroot;
    }
}
