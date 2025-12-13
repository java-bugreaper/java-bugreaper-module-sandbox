package core;

import io.bugreaper.core.config.YamlUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Link;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.bugreaper.core.allurereporter.AllureBuilder.reportHtmlCsvCases;
import static io.bugreaper.core.allurereporter.AllureReporter.*;
import static io.bugreaper.core.filereaders.FileReader.readCsvToArray;
import static io.bugreaper.core.filereaders.FileReader.readTextFromFile;
import static io.bugreaper.core.filereaders.ResourcesFileReader.readResourceFile;

@Tag("core")
@Epic("[core] Tests core only")
final public class CoreTest {

    @Test
    @Link("1")
    @DisplayName("Decision Table csv example")
    @Description("Example of decision table template from csv and base csv attach")
    void csvExample() {
        reportHtmlCsvCases(
                "Test cases example",
                readCsvToArray("data/csv/test_csv.csv"));

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
}
