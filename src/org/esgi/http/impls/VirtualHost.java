package org.esgi.http.impls;

import org.codehaus.jackson.map.ObjectMapper;
import org.esgi.http.interfaces.IVirtualHost;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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

    public HashMap<String, String> getHostList()
    {
        HashMap<String, String> hostList = new HashMap<String, String>();
        ObjectMapper objectMapper = new ObjectMapper();
        VirtualHost vh = null;
        try
        {
            vh = objectMapper.readValue(new File("config.js"), VirtualHost.class);
            for(int i=0; i<vh.getHosts().length;i++)
            {
                if((!vh.getHosts()[i].equals("}") || !vh.getHosts()[i].equals("}")) && vh.getHosts()[i].equals("name"))
                {

                    hostList.put(vh.getHosts()[i+1], vh.getHosts()[i+3]);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return hostList;
    }
}
