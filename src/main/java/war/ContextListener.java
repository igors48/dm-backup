package war;

import ognl.OgnlRuntime;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by igor on 08.04.2017.
 */
public class ContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        OgnlRuntime.setSecurityManager(null);
    }

}