package service;

/**
 * Created by igor on 16.11.2016.
 */
public class FormParameters {

    public final String url;
    public final String referer;
    public final String data;

    public FormParameters(final String url, final String referer, final String data) {
        this.url = url;
        this.referer = referer;
        this.data = data;
    }

}
