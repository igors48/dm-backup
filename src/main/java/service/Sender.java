package service;

/**
 * Created by igor on 14.11.2016.
 */
public interface Sender {

    void sendContent(String recipient, String content) throws ServiceException;

    void sendError(String recipient, ServiceError error) throws ServiceException;

}
