package service;

import com.google.appengine.api.utils.SystemProperty;
import service.error.SendingException;
import service.error.ServiceException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import static util.Assert.guard;
import static util.Parameter.*;

/**
 * Created by igor on 14.11.2016.
 */
public class Sender {

    private static final Logger LOGGER = Logger.getLogger(Sender.class.getName());

    private static final String DATE_FORMAT_FOR_NAME = "yyyy-MM-dd'T'HH-mm-ss";
    private static final String DATE_FORMAT_FOR_BODY = "yyyy-MM-dd HH:mm:ss";
    private static final String CSV = ".csv";

    private final String version;

    public Sender(final String version) {
        guard(isValidString(this.version = version));
    }

    public void sendDailyBackup(final String sender, final String recipient, final Content content) throws ServiceException {
        guard(isValidEmail(sender));
        guard(isValidEmail(recipient));
        guard(notNull(content));

        LOGGER.info(String.format("Sending daily backup to [ %s ]", recipient));
    }

    public void sendChangedContent(final String sender, final String recipient, final Content content) throws ServiceException {
        guard(isValidEmail(sender));
        guard(isValidEmail(recipient));
        guard(notNull(content));

        LOGGER.info(String.format("Sending changed content to [ %s ]", recipient));
    }

    private void sendContent(final String sender, final String recipient, final Content content) throws ServiceException {
        guard(isValidEmail(sender));
        guard(isValidEmail(recipient));
        guard(notNull(content));

        LOGGER.info(String.format("Sending content to [ %s ]", recipient));

        final Date now = new Date();

        final SimpleDateFormat formatForName = new SimpleDateFormat(DATE_FORMAT_FOR_NAME);
        final String dateForName = formatForName.format(now);
        final String attachmentName = dateForName + CSV;

        final SimpleDateFormat formatForBody = new SimpleDateFormat(DATE_FORMAT_FOR_BODY);
        final String dateForBody = formatForBody.format(now);
        final String subject = "File backed up at " + dateForBody;
        final String applicationId = SystemProperty.applicationId.get();

        final String body = Template.formatContent(dateForBody, applicationId, content.accounts, version);

        sendMail(sender, recipient, subject, body, attachmentName, content.file);
    }

    public void sendException(final String recipient, final ServiceException exception) throws ServiceException {
        guard(isValidEmail(recipient));
        guard(notNull(exception));

        LOGGER.info(String.format("Sending error message to [ %s ]", recipient));

        sendMail(recipient, recipient, "Backup error", exception.toString(), null, null);
    }

    private static void sendMail(final String sender, final String recipient, final String subject, final String body, final String attachmentName, final String attachmentContent) throws ServiceException {

        try {
            final Properties props = new Properties();
            final Session session = Session.getDefaultInstance(props, null);

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            message.setSubject(subject);

            final Multipart multipart = new MimeMultipart();

            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body, "text/html");
            multipart.addBodyPart(htmlPart);

            if (attachmentContent != null) {
                final MimeBodyPart attachment = new MimeBodyPart();

                attachment.setFileName(attachmentName);
                DataSource dataSource = new ByteArrayDataSource(attachmentContent.getBytes("UTF-8"), "application/octet-stream");
                attachment.setDataHandler(new DataHandler(dataSource));
                multipart.addBodyPart(attachment);
            }

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception exception) {
            throw new SendingException(exception.getMessage());
        }
    }

}


