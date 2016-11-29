package gae;

/**
 * Author : Igor Usenko ( igors48@gmail.com )
 * Date : 20.03.2014
 */
public enum Kind {

    ROOT("Root"),
    TIMESTAMP("Timestamp"),
    SNAPSHOT("Snapshot");

    public final String value;

    Kind(final String value) {
        this.value = value;
    }

}
