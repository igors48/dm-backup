package service.error;

import service.ServiceException;

/**
 * Created by igor on 16.11.2016.
 */
public class InvalidContent extends ServiceException {

    private final String content;

    public InvalidContent(final String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvalidContent that = (InvalidContent) o;

        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "InvalidContent{" +
                "content='" + content + '\'' +
                '}';
    }

}
