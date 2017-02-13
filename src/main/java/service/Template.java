package service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import util.account.Account;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by igor on 01.02.2017.
 */
public class Template {

    public static String formatContent(final String time, final String server, final List<Account> accounts) {
        final Context context = new Context(Locale.ROOT);

        context.setVariable("backupTime", time);
        context.setVariable("serverName", server);
        context.setVariable("accounts", accounts);

        return createEngine().process("content", context);
    }

    private static TemplateEngine createEngine() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("templates\\");
        templateResolver.setSuffix(".txt");

        final TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver);

        return engine;
    }

}
