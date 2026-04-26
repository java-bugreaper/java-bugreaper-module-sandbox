package core;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Link;
import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.modules.filehelper.FileHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.allurereporter.AllureBuilder.reportHtmlCsvCases;
import static net.bugreaper.core.allurereporter.AllureReporter.*;
import static net.bugreaper.core.filereaders.FileReader.readCsvToArray;
import static net.bugreaper.core.filereaders.FileReader.readTextFromFile;
import static net.bugreaper.core.filereaders.ResourcesFileReader.createResourceFileWithSize;
import static net.bugreaper.core.filereaders.ResourcesFileReader.readResourceFile;

@Tag("core")
@Epic("[core] FileHelper submodule")
final public class FileHelperTest {

    FileHelper fileHelper = new FileHelper();

    public static final String MESSAGE = "some message";
    public static final String TEMP_FILE = "temp/my_temp.txt";
    public static final String HASH_FILE = "files/test_hash.txt";

    @Test
    @DisplayName("Create file and check data")
    void createFileAndCheckTest() {
        fileHelper.cleanFile(TEMP_FILE);
        fileHelper.addToFile(TEMP_FILE, MESSAGE);

        fileHelper.seeFileContainString(TEMP_FILE, MESSAGE);

    }

    @Test
    @DisplayName("Check file hashes")
    void checkFileHashesTest() {
        fileHelper.seeFileHashMd5Equal(HASH_FILE, "b156152dfc1637561d6bab4e6bd1aee3");
        fileHelper.seeFileHashSha256Equal(HASH_FILE, "fc123f3d794f32cb31cf63808fcd70131211bac27afaba12a47fa48981175d24");
    }

}
