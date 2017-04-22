package service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import util.account.Account;
import util.account.AccountsParser;
import util.account.ComparisonResult;
import util.account.ParsedAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static util.Assert.guard;
import static util.Parameter.isValidString;
import static util.Parameter.notNull;

/**
 * Created by igor on 01.02.2017.
 */
public class Template {

    public static final String GREEN = "#00dd00";
    public static final String GRAY = "#dddddd";
    public static final String RED = "#dd0000";

    public static String formatError(final String time, final String server, final String description, final String version) {
        guard(isValidString(time));
        guard(isValidString(server));
        guard(notNull(description));
        guard(isValidString(version));

        final Context context = new Context(Locale.ROOT);

        context.setVariable("caption", "Unexpected error");
        context.setVariable("time", time);
        context.setVariable("server", server);
        context.setVariable("description", description);
        context.setVariable("version", version);

        final TemplateEngine engine = createEngine();

        final String error = engine.process("error", context);
        context.setVariable("content", error);

        return engine.process("container", context);
    }

    public static String formatContent(final String caption, final String time, final String server, final List<Account> accounts, final List<Account> previousAccounts, final String version) {
        guard(notNull(caption));
        guard(notNull(time));
        guard(notNull(server));
        guard(notNull(accounts));
        guard(notNull(previousAccounts));

        final Context context = new Context(Locale.ROOT);

        context.setVariable("caption", caption);
        context.setVariable("time", time);
        context.setVariable("server", server);
        context.setVariable("version", version);

        final TemplateEngine engine = createEngine();

        if (previousAccounts.isEmpty()) {
            final List<ParsedAccount> parsedAccounts = ParsedAccount.createList(accounts);
            context.setVariable("accounts", parsedAccounts);

            final String content = engine.process("content", context);
            context.setVariable("content", content);
        } else {
            final List<ParsedAccount> before = ParsedAccount.createList(previousAccounts);
            final List<ParsedAccount> after = ParsedAccount.createList(accounts);
            final List<ComparisonResult> comparison = AccountsParser.compare(before, after);
            final List<Difference> differences = render(comparison);

            context.setVariable("differences", differences);

            final String content = engine.process("changed-content", context);
            context.setVariable("content", content);
        }

        return engine.process("container", context);
    }

    private static List<Difference> render(final List<ComparisonResult> comparison) {
        final List<Difference> result = new ArrayList<>();

        for (final ComparisonResult current : comparison) {
            result.add(render(current));
        }

        return result;
    }

    public static Difference render(final ComparisonResult current) {
        guard(notNull(current));

        final String title = current.title;
        final String before = formatCurrency(current.before);
        final String after = formatCurrency(current.after);
        final String delta = formatCurrency(current.delta);
        final String color = getColorFor(current.delta);

        return new Difference(title, before, after, delta, color);
    }

    private static String getColorFor(Float delta) {

        if (delta < 0) {
            return RED;
        }

        if (delta < 0.01) {
            return GRAY;
        }

        return GREEN;
    }

    private static String formatCurrency(final Float value) {

        if (value == null) {
            return "";
        }

        return String.format("%.02f", value);
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
