package service.error;

import service.ServiceException;

/**
 * Created by igor on 19.11.2016.
 */
public class InvalidConfigurationParameter extends ServiceException {

    private final String parameter;
    private final String value;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvalidConfigurationParameter that = (InvalidConfigurationParameter) o;

        if (parameter != null ? !parameter.equals(that.parameter) : that.parameter != null) return false;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = parameter != null ? parameter.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);

        return result;
    }

}
