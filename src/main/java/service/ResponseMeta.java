package service;

/**
 * Created by igor on 16.11.2016.
 */
public class ResponseMeta {

    public final int statusCode;
    public final String contentType;

    public ResponseMeta(final int statusCode, final String contentType) {
        this.statusCode = statusCode;
        this.contentType = contentType;
    }

}
