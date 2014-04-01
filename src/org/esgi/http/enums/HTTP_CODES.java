package org.esgi.http.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Voodoo
 * Date: 01/04/14
 * Time: 19:59
 * To change this template use File | Settings | File Templates.
 */
public enum HTTP_CODES {
    OK("OK", 200), NOT_FOUND("Not Found", 404);

    private String value;
    private int code;

    private HTTP_CODES(String val, int c) {
        value = val;
        code = c;
    }

    public int getCode(){ return code;}

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%d %s",code,value);
    }
}
