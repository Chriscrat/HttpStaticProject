package org.esgi.http.impls;

import org.esgi.http.interfaces.ICookie;

/**
 * Created with IntelliJ IDEA.
 * User: FuglyLionKing
 * Date: 01/04/14
 * Time: 09:16
 * To change this template use File | Settings | File Templates.
 */
public class Cookie implements ICookie{
    private String value;
    private String name;

    public Cookie(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
}
