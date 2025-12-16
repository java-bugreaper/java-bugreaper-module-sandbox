package db.failed;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import setup.db.SqlMaria;

@Tag("failed")
@Epic("[db-failed] Expected failed database tests")
@Feature("Db checks: MariaDb failed")
class MariaDbFailedTest {


    protected final SqlMaria sqlMaria = SqlMaria.getInstance();

    @BeforeEach
    @Step("Clear test data")
    public void clean() {
        sqlMaria.cleanMariaDb();
    }

    @Test
    @Description("""
            Create data in MariaDb table broken
            * duplicate column (no sql execute)""")
    void checkMariaDb_fixtureWithDuplicateColumn_broken() {

        final var name = "Alex";

        sqlMaria.insertIntoUsers(new Object[][]{
                {"USERNAME", name},
                {"USERNAME", "second name"},
                {"DESCRIPTION", null},
        });
    }

    @Test
    @Description("""
            Create data in MariaDb table broken
            * not exists column (no sql execute)""")
    void checkMariaDb_fixtureWithNotExistsColumn_broken() {

        final var name = "Alex";

        sqlMaria.insertIntoUsers(new Object[][]{
                {"USERNAME", name},
                {"WRONG_COLUMN", 1},
        });
    }

    @Test
    @Description("""
            Assert count from table without conditions failed""")
    void checkMariaDb_tableNotEmptyCheck_failed() {

        sqlMaria.assertCountFromUsers(4);
    }

    @Test
    @Description("""
            Create data in MariaDb table
            -> check data in MariaDb table failed (iSeeDb feature)""")
    void checkMariaDb_fixtureCreate_dataExistCheck_failed() {

        final var id = 15;
        final var name = "Alex";
        final var email = "alex@com.com";

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
                {"EMAIL", email},
                {"DESCRIPTION", null},
                {"UPDATE_DATE", "2025-05-05"},
        });


        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", id},
                {"USERNAME", "LIKE", "Pupkin"},
                {"EMAIL", email},
                {"DESCRIPTION", null},
        });
    }

    @Test
    @Description("""
            Create JSON data in MariaDb table
            -> check JSON data in MariaDb table JSON contains failed""")
    void checkMariaDb_jsonDataCheck_jsonContains_failed() {

        final var id2 = 20;

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id2},
                {"DATA", """
                        {"id":1,"text":"test"}"""},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", id2},
                {"DATA", "JSON_CONTAINS",
                        """
                        {"id":2}"""},
        });
    }

    @Test
    @Description("""
            Create JSON data in MariaDb table
            -> check JSON data in MariaDb table JSON equals failed""")
    void checkMariaDb_jsonDataCheck_jsonEqual_failed() {

        final var id2 = 20;

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id2},
                {"DATA", """
                        {"id":1,"text":"test"}"""},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", id2},
                {"DATA", "JSON_EQUALS",
                        """
                        {"id":1}"""},
        });
    }

    @Test
    @Description("""
            Create JSON data in MariaDb table
            -> check JSON data in MariaDb table JSON wrong operator exception""")
    void checkMariaDb_jsonDataCheck_wrongOperator() {

        final var id2 = 20;

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id2},
                {"DATA", """
                        {"id":1,"text":"test"}"""},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", id2},
                {"DATA", "JSON_WRONG",
                        """
                        {"id":1}"""},
        });
    }

    @Test
    @Description("""
            Create JSON data in MariaDb table
            -> check JSON data in MariaDb table JSON contains""")
    void checkMariaDb_jsonContains_invalidJsonValidation() {

        final var id2 = 20;

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id2},
                {"DATA", """
                        {"id":1,"text":"test"}"""},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", id2},
                {"DATA", "JSON_CONTAINS",
                        """
                        {"id":1,"text":"test",}"""},
        });
    }

}
