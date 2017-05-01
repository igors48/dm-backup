package service;

import com.google.appengine.api.utils.SystemProperty;
import service.error.SendingException;
import service.error.ServiceException;
import util.account.Account;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

        this.sendContent("Daily backup", sender, recipient, content, new ArrayList<Account>());
    }

    public void sendChangedContent(final String sender, final String recipient, final Content content, List<Account> previousAccounts) throws ServiceException {
        guard(isValidEmail(sender));
        guard(isValidEmail(recipient));
        guard(notNull(content));
        guard(notNull(previousAccounts));

        LOGGER.info(String.format("Sending changed content to [ %s ]", recipient));

        this.sendContent("Changed content", sender, recipient, content, previousAccounts);
    }

    private void sendContent(final String caption, final String sender, final String recipient, final Content content, final List<Account> previousAccounts) throws ServiceException {
        guard(notNull(caption));
        guard(isValidEmail(sender));
        guard(isValidEmail(recipient));
        guard(notNull(content));
        guard(notNull(previousAccounts));

        final Date now = new Date();

        final SimpleDateFormat formatForName = new SimpleDateFormat(DATE_FORMAT_FOR_NAME);
        final String dateForName = formatForName.format(now);
        final String attachmentName = dateForName + CSV;

        final SimpleDateFormat formatForBody = new SimpleDateFormat(DATE_FORMAT_FOR_BODY);
        final String dateForBody = formatForBody.format(now);
        final String subject = caption + " " + dateForBody;
        final String applicationId = SystemProperty.applicationId.get();

        final String body = Template.formatContent(caption, dateForBody, applicationId, content.accounts, previousAccounts, dateForBody, dateForBody, version);

        sendMail(sender, applicationId, recipient, subject, body, attachmentName, content.file);
    }

    public void sendException(final String recipient, final ServiceException exception) throws ServiceException {
        guard(isValidEmail(recipient));
        guard(notNull(exception));

        final Date now = new Date();

        LOGGER.info(String.format("Sending error message to [ %s ]", recipient));

        final SimpleDateFormat formatForBody = new SimpleDateFormat(DATE_FORMAT_FOR_BODY);
        final String dateForBody = formatForBody.format(now);
        final String caption = "Backup error";
        final String subject = caption + " " + dateForBody;
        final String applicationId = SystemProperty.applicationId.get();

        final String body = Template.formatError(dateForBody, applicationId, exception.toString(), this.version);

        sendMail(recipient, applicationId, recipient, subject, body, null, null);
    }

    private static void sendMail(final String sender, final String senderName, final String recipient, final String subject, final String body, final String attachmentName, final String attachmentContent) throws ServiceException {

        try {
            final Properties props = new Properties();
            final Session session = Session.getDefaultInstance(props, null);

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender, senderName));
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


