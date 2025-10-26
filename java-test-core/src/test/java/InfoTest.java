import io.qameta.allure.Epic;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.bugreaper.core.allurereporter.AllureReporter.attachFromFile;

@Epic("[info] Info")
@Order(1)
final public class InfoTest {


    @Test
    @Issue("2")
    @DisplayName("Read some json example")
    void showJsonExample() {
        attachFromFile(
                "json example",
                "data/mocks/post_200.json");
    }

    @Test
    @Link("1")
    @DisplayName("Read some config example")
    void showConfigExample() {
        attachFromFile(
                "config yml example",
                "dummy/app-config.yml");
    }

}
