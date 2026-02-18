package id.ac.ui.cs.advprog.eshop;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EshopApplicationTests {

    @Test
    void contextLoads() {
    }

    @AfterEach
    void tearDown() {
        System.clearProperty("spring.main.web-application-type");
        System.clearProperty("app.closeAfterRun");
    }

    @Test
    void main_startsAndExitsCleanly() {
        // Supaya tidak start web server (lebih ringan)
        System.setProperty("spring.main.web-application-type", "none");
        // Supaya main() langsung close context (tidak ninggalin proses hidup)
        System.setProperty("app.closeAfterRun", "true");

        EshopApplication.main(new String[]{});
    }

    @Test
    void main_startsWithoutAutoClose_doesNotHang() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            System.setProperty("spring.main.web-application-type", "none");
            System.clearProperty("app.closeAfterRun"); // pastikan false branch
            EshopApplication.main(new String[]{});
        });
    }
}
