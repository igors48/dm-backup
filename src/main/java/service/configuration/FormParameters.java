package service.configuration;

import static util.Assert.guard;
import static util.Parameter.isValidFormData;
import static util.Parameter.isValidUrl;

/**
 * Created by igor on 16.11.2016.
 */
public class FormParameters {

    public final String url;
    public final String referer;
    public final String data;

    public FormParameters(final String url, final String referer, final String data) {
        guard(isValidUrl(this.url = url));
        guard(isValidUrl(this.referer = referer));
        guard(isValidFormData(this.data = data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormParameters that = (FormParameters) o;

        if (!url.equals(that.url)) return false;
        if (!referer.equals(that.referer)) return false;

        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + referer.hashCode();
        result = 31 * result + data.hashCode();

        return result;
    }

}
