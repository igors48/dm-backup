package service.configuration;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 16.11.2016.
 */
public class AccessParameters {

    public final GeneralParameters general;
    public final FormParameters login;
    public final FormParameters download;

    public AccessParameters(final GeneralParameters general, final FormParameters login, final FormParameters download) {
        guard(notNull(this.general = general));
        guard(notNull(this.login = login));
        guard(notNull(this.download = download));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessParameters that = (AccessParameters) o;

        if (!general.equals(that.general)) return false;
        if (!login.equals(that.login)) return false;

        return download.equals(that.download);
    }

    @Override
    public int hashCode() {
        int result = general.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + download.hashCode();

        return result;
    }

}
