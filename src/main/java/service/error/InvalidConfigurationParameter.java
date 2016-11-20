package service.error;

import service.ServiceException;

/**
 * Created by igor on 19.11.2016.
 */
public class InvalidConfigurationParameter extends ServiceException {

    public final String parameter;
    public final String value;

    public InvalidConfigurationParameter(final String parameter, final String value) {
        this.parameter = parameter;
        this.value = value;
    }

    @Override
    public String toString() {
        return "InvalidConfigurationParameter{" +
                "parameter='" + parameter + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
