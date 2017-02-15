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
    public final FormParameters accounts; //TODO replace with more specific class for XPath

    public AccessParameters(final GeneralParameters general, final FormParameters login, final FormParameters download, final FormParameters accounts) {
        guard(notNull(this.general = general));
        guard(notNull(this.login = login));
        guard(notNull(this.download = download));
        guard(notNull(this.accounts = accounts));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessParameters that = (AccessParameters) o;

        if (!general.equals(that.general)) return false;
        if (!login.equals(that.login)) return false;
        if (!download.equals(that.download)) return false;

        return accounts.equals(that.accounts);
    }

    @Override
    public int hashCode() {
        int result = general.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + download.hashCode();
        result = 31 * result + accounts.hashCode();

        return result;
    }

}
