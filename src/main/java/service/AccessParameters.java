package service;

/**
 * Created by igor on 16.11.2016.
 */
public class AccessParameters {

    public final GeneralParameters general;
    public final FormParameters login;
    public final FormParameters download;

    public AccessParameters(final GeneralParameters general, final FormParameters login, final FormParameters download) {
        this.general = general;
        this.login = login;
        this.download = download;
    }

}
