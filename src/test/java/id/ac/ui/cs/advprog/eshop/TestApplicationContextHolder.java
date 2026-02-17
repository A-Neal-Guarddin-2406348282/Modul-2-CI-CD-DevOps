package id.ac.ui.cs.advprog.eshop;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

@TestComponent
public class TestApplicationContextHolder implements ApplicationContextAware {
    private static volatile ConfigurableApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (applicationContext instanceof ConfigurableApplicationContext configurable) {
            context = configurable;
        }
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    public static void clear() {
        context = null;
    }
}