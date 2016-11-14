package service;

/**
 * Created by igor on 14.11.2016.
 */
public interface Sender {

    void sendContent(String content) throws ServiceException;

    void sendError(ServiceError error) throws ServiceException;

}
