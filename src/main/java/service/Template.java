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

    private final TemplateEngine engine;

    public Template() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("templates\\");
        templateResolver.setSuffix(".txt");

        this.engine = new TemplateEngine();
        this.engine.setTemplateResolver(templateResolver);
    }

    public String formatContent() {
        final Context context = new Context(Locale.ROOT);
        context.setVariable("backupTime", "12:34");
        context.setVariable("serverName", "wonderserver");

        List<Account> accounts = Arrays.asList(new Account("a", "b"), new Account("c", "d"));
        context.setVariable("accounts", accounts);

        return this.engine.process("content", context);
    }
}
