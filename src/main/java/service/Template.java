package service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import util.account.Account;

import java.util.List;
import java.util.Locale;

import static util.Assert.guard;
import static util.Parameter.notNull;

/**
 * Created by igor on 01.02.2017.
 */
public class Template {

    public static String formatContent(final String caption, final String time, final String server, final List<Account> accounts, final String version) {
        guard(notNull(caption));
        guard(notNull(time));
        guard(notNull(server));
        guard(notNull(accounts));

        final Context context = new Context(Locale.ROOT);

        context.setVariable("caption", caption);
        context.setVariable("time", time);
        context.setVariable("server", server);
        context.setVariable("accounts", accounts);
        context.setVariable("version", version);

        final TemplateEngine engine = createEngine();

        final String content = engine.process("content", context);
        context.setVariable("content", content);

        return engine.process("container", context);
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
