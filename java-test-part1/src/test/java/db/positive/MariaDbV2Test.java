package db.positive;

import io.qameta.allure.Step;
import net.bugreaper.modules.db.MariaDb;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) //To call connection once!
@Feature("Db checks: MariaDb vs simple data provide")
class MariaDbV2Test {

    //private static final MariaDb maria = new MariaDb();
    private final MariaDb maria = new MariaDb();

    @BeforeEach
    @Step("Clear test data")
    public void clean() {
        maria.truncateTable("MARIA_SCHEMA.USERS");
    }

    @Test
    @Description("""
            Show filed and empty table data""")
    void showTableDataTest() {

        maria.showDataFromTable("MARIA_SCHEMA.CATEGORY_DICT");

        maria.showDataFromTable("MARIA_SCHEMA.USERS");
    }


    @Test
    @Description("""
            Insert from template file + test data by Object (different column types)
             -> check data by Object;
             Good for dynamic data(envs) easy to provide data in object""")
    void checkMariaDb_getTemplateAndProvideObject() {

        final var id = 15;
        final var name = "Alex";
        final var email = "alex@com.com";
        final var amount = 10.66;

        maria.insertIntoTable("MARIA_SCHEMA.USERS",
                new Object[][]{
                        {"ID", id},
                        {"USERNAME", name},
                        {"EMAIL", email},
                        {"STATE", true},
                        {"IS_ACTIVE", false},
                        {"AMOUNT", amount},
                        {"DATA", """
                                    {
                                     "id": 1,
                                     "text": "test"
                                    }"""},
                        {"DESCRIPTION", null},
                });


        maria.seeRecordExistsInTable("MARIA_SCHEMA.USERS",
                new Object[][]{
                        {"ID", id},
                        {"USERNAME", name},
                        {"EMAIL", email},
                        {"STATE", true},
                        {"IS_ACTIVE", false},
                        {"AMOUNT", amount},
                        {"DATA", "JSON_CONTAINS",
                                """
                                    {
                                     "id": 1,
                                     "text": "test"
                                    }"""},
                        {"DESCRIPTION", null},
                });
    }

    @Test
    @Description("""
            Insert from template file + test data by JSON (different column types)
             -> check data  by JSON;
             Good for static data, easy to use""")
    void checkMariaDb_getTemplateAndProvideJson() {

        maria.insertIntoTable("MARIA_SCHEMA.USERS",
                """
                        {
                            "ID": 15,
                            "USERNAME": "Alex",
                            "EMAIL": "alex@com.com",
                            "STATE": true,
                            "IS_ACTIVE": true,
                            "AMOUNT": 15.77,
                            "DATA": {
                                     "id": 1,
                                     "text": "test"
                                     },
                            "DESCRIPTION": null
                        }""");


        maria.seeRecordExistsInTable("MARIA_SCHEMA.USERS",
                """
                        {
                            "ID": 15,
                            "USERNAME": "Alex",
                            "EMAIL": "alex@com.com",
                            "STATE": true,
                            "IS_ACTIVE": true,
                            "AMOUNT": 15.77,
                            "DATA:JSON_CONTAINS": {
                                     "id": 1
                                     },
                            "DESCRIPTION": null
                        }""");

    }

    @Test
    @Description("""
            Check assert failed with custom and global await""")
    void checkCustomAwait() {

        maria.insertIntoTable("MARIA_SCHEMA.USERS",
                new Object[][]{
                        {"ID", 1},
                });


        assertThrows(AssertionError.class, () -> maria.withAwaitMs(500).seeRecordExistsInTable("MARIA_SCHEMA.USERS",
                new Object[][]{
                        {"ID", 888},
                }));

        assertThrows(AssertionError.class, () ->
                maria.seeRecordExistsInTable("MARIA_SCHEMA.USERS",
                        new Object[][]{
                                {"ID", 999},
                        }));
    }
}
