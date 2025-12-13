package db.positive;

import db.TestsSetupDB;
import io.bugreaper.modules.db.MariaDb;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;


@Feature("Db checks: MariaDb vs simple data provide")
class MariaDbV2Test extends TestsSetupDB {

    MariaDb maria = new MariaDb();

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
                        {"DATA",   """
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
}
