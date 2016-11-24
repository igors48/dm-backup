package service.configuration;

import static util.Assert.guard;
import static util.Parameter.isValidDomain;
import static util.Parameter.isValidUrl;

/**
 * Created by igor on 16.11.2016.
 */
public class GeneralParameters {

    public final String origin;
    public final String host;

    public GeneralParameters(final String origin, final String host) {
        guard(isValidUrl(this.origin = origin));
        guard(isValidDomain(this.host = host));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralParameters that = (GeneralParameters) o;

        if (!origin.equals(that.origin)) return false;

        return host.equals(that.host);
    }

    @Override
    public int hashCode() {
        int result = origin.hashCode();
        result = 31 * result + host.hashCode();

        return result;
    }

}
