package com.finance;

import com.finance.service.LancamentoService;
import com.finance.ui.FinanceWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import javax.swing.SwingUtilities;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
        basePackages = "com.finance",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = FinanceApplication.class
        )
)
public class FinanceDesktopApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ConfigurableApplicationContext context = SpringApplication.run(FinanceDesktopApplication.class, args);
        LancamentoService service = context.getBean(LancamentoService.class);

        SwingUtilities.invokeLater(() -> {
            FinanceWindow window = new FinanceWindow(service);
            window.setVisible(true);
        });
    }
}
