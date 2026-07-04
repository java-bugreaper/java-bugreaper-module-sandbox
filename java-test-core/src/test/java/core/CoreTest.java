package core;

import net.bugreaper.core.config.YamlUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Link;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.allurereporter.AllureReporter.*;
import static net.bugreaper.core.filereaders.FileReader.readTextFromFile;
import static net.bugreaper.core.filereaders.ResourcesFileReader.*;

@Tag("core")
@Epic("[core] Tests core only")
final public class CoreTest {

    @Test
    @Link("1")
    @DisplayName("Decision Table csv example")
    @Description("Example of decision table template from csv and base csv attach")
    void csvExample() {

        attachFromCsv(
                "Test cases example",
                "data/csv/test_csv.csv");

        Allure.addAttachment("file source v1", "text/csv", readTextFromFile("data/csv/test_csv.csv"));
        Allure.addAttachment("file source v2", "text/csv", readResourceFile("data/csv/test_csv.csv"));
    }

    @Test
    @Description("Read custom config field")
    void customConfigFields() {

        String path = "custom-module.v1.test";

        attachFromFile(
                "bugreaper config",
                "bugreaper.yml");

        String ar = YamlUtils.getStringValueByPath(path);

        reporter(path + " = " + ar);

    }

    @Test
    @Description("Example of allure steps and attachments")
    void attachmentsCheck() {

        attachFromFile(
                "With step",
                "bugreaper.yml");

        attachFromFileNoStep(
                "No step",
                "bugreaper.yml");
    }

    @Test
    @DisplayName("Create file with specific size ( No steps )")
    void checkFileSizeTest() {
        String fileName = "temp/file_size.txt";

        createResourceFileWithSize(fileName, 2000);

        seeResourceFileNotEmpty(fileName);
    }

}
