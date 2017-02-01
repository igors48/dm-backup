package service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Created by igor on 01.02.2017.
 */
public class Template {

    private final TemplateEngine engine;

    public Template() {
        ITemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        this.engine = new TemplateEngine();
        this.engine.setTemplateResolver(templateResolver);
    }

    public String formatContent() {
        return this.engine.process("templates\\content.txt", new Context());
    }
}
