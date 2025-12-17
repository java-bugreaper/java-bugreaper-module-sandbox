package db.positive;

import db.SqlMy;
import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.allurereporter.AllureReporter.reporter;

@Tag("db")
@Epic("[db] Tests for DataBases")
@Feature("Db checks: MySql")
class MySqlTest {

    protected final SqlMy mysql = SqlMy.getInstance();

    protected static final String TEST_10_MINUTES = "2025-01-01 00:10:00";
    protected static final String TEST_20_MINUTES = "2025-01-01 00:20:00";
    protected static final String TEST_30_MINUTES = "2025-01-01 00:30:00";


    @BeforeEach
    @Step("Clear test data")
    public void clean() {
        mysql.cleanMySql();
    }

    @AfterEach
    @Step("Logs after tests")
    public void getLogs() {
        reporter("Here you can get some logs or other info");
        mysql.iSeeInTableUsers();
    }



    @Test
    @Description("""
            Create data in MySql table
            -> check data in MySql table""")
    void checkMySql_fixtureCreate_dataExistCheck_links() {

        final var id = 15;
        final var name = "Alex";
        final var email = "alex@com.com";
        final var amount = 11.99;

        mysql.insertIntoUsers(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
                {"EMAIL", email},
                {"STATE", true},
                {"AMOUNT", amount},
                {"DESCRIPTION", null},
        });


        mysql.recordUsers_exist(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
                {"EMAIL", email},
                {"STATE", true},
                {"AMOUNT", amount},
                {"DESCRIPTION", null},
        });

    }

    @Test
    @Description("""
            Create JSON data in MySql table
            -> check JSON data in MySql table JSON contains""")
    void checkMySql_jsonDataCheck_jsonContains() {

        final var id2 = 20;

        mysql.insertIntoUsers(new Object[][]{
                {"ID", id2},
                {"DATA", """
                        {"id":1,"text":"test"}"""},
        });

        mysql.recordUsers_exist(new Object[][]{
                {"ID", id2},
                {"DATA", "JSON_CONTAINS", """
                    {"id":1}"""},
        });
    }

    @Test
    @Description("""
            Create JSON array data in MySql table
            -> check JSON data in MySql table JSON contains""")
    void checkMySql_jsonArrayDataCheck_jsonContains() {

        final var id2 = 20;

        mysql.insertIntoUsers(new Object[][]{
                {"ID", id2},
                {"DATA", """
                        [{"id":1,"text":"test"}, {"id":2,"text":"test2"}]"""},
        });

        mysql.recordUsers_exist(new Object[][]{
                {"ID", id2},
                {"DATA", "JSON_CONTAINS", """
                    [{"id":1}]"""},
        });
    }

    @Test
    @Description("""
            Create data in MySql table
            -> check Date data match regex""")
    void checkMySql_regexDateDataCheck() {


        mysql.insertIntoUsers(new Object[][]{
                {"ID", "1"},
                {"BIRTHDAY", "1986-05-01"},
        });

        mysql.recordUsers_exist(new Object[][]{
                {"ID", "1"},
                {"BIRTHDAY", "REGEX_MATCH", "[0-9]{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])"},
        });
    }

    @Test
    @Description("""
            Create data in MySql table (2 records)
            -> check records rows in MySql table""")
    void checkMySql_fixtureCreate_dataCountCheck() {

        mysql.insertIntoUsers(new Object[][]{
                {"ID", "1"},
        });
        mysql.insertIntoUsers(new Object[][]{
                {"ID", "2"},
                {"USERNAME", "Mark"},
        });

        mysql.assertCountFromUsers(2);
    }


    @Test
    @Description("""
            Create data with DateTime in MySql table
            -> check data in MySql table with three elements""")
    void checkMySql_fixtureDateTimeCreate_dataExistWith3ElementsCheck() {

        final var name = "Alex";

        mysql.insertIntoUsers(new Object[][]{
                {"USERNAME", name},
                {"CREATE_DATE", TEST_20_MINUTES},
                {"UPDATE_DATE", TEST_10_MINUTES},
        });

        mysql.recordUsers_exist(new Object[][]{
                {"USERNAME", name},
                {"CREATE_DATE", TEST_20_MINUTES},
                {"CREATE_DATE", ">=", TEST_10_MINUTES},
                {"UPDATE_DATE", "<", TEST_30_MINUTES},
        });

    }

    @Test
    @Description("""
            Create data in MySql table
            -> check data by hardcode condition""")
    void checkMySql_fixture_dataExistWithHardcodeConditionCheck() {

        final var name = "Alex";

        mysql.insertIntoUsers(new Object[][]{
                {"USERNAME", name},
                {"DEPOSIT", "55.00"},
        });

        mysql.recordUsers_exist("where ID >=1 OR DEPOSIT > 10");
    }

    @Test
    @Description("""
            Check data in MySql table (dump) with all columns info""")
    void checkMySql_checkDumpExist() {

        mysql.iSeeInCategoryDump(new Object[][]{
                {"NAME", "SSD"},
                {"STATE", true},
        });

        mysql.iSeeInCategoryDump(new Object[][]{
                {"NAME", "Monitors"},
                {"ACTIVE_DATE", "<", "2025-12-12"},
                {"ACTIVE_DATE", ">=", "1999-01-01 00:00:00"},
        });

    }

    @Test
    @Description("""
            Delete data in MySql table by conditions
            -> check data in MySql table""")
    void checkMySql_deleteByConditions() {

        final var id = "15";
        final var name = "Alex";

        mysql.insertIntoUsers(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
        });
        mysql.insertIntoUsers(new Object[][]{
                {"ID", "2"},
                {"USERNAME", "dummy"},
        });

        mysql.recordUsers_delete(
                new Object[][]{
                        {"ID", id},
                        {"USERNAME", name},
                }
        );

        mysql.recordUsers_notExist(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
        });

        mysql.recordUsers_exist(new Object[][]{
                {"ID", "2"},
                {"USERNAME", "dummy"},
        });

    }

    @Test
    @Description("""
            Update data in MySql table by conditions
            -> check data in MySql table""")
    void checkMySql_updateByConditions() {

        final var id = 15;
        final var name = "Alex";
        final var email = "alex@com.com";

        mysql.insertIntoUsers(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
                {"EMAIL", email},
        });

        mysql.recordUsers_update(
                new Object[][]{
                        {"USERNAME", name},
                        {"EMAIL", email},
                },
                new Object[][]{
                        {"ID", id},
                }
        );

        mysql.recordUsers_exist(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
                {"EMAIL", email},
        });

    }

    @Test
    @Link("5")
    @Description("""
            Insert multiple records""")
    void insertMultipleRecords() {

        int RECORDS_COUNT = 20;

        Allure.step("(DB) Insert multiple records into USERS: " + RECORDS_COUNT, step -> {

            for (int i = 1; i <= RECORDS_COUNT; i++) {

                mysql.insertIntoUsers(new Object[][]{
                        {"ID", String.valueOf(i)},
                        {"USERNAME", "Alex_" + i},
                        {"EMAIL", "alex_" + i + "@com.com"},
                });
            }
        });

        mysql.assertCountFromUsers(RECORDS_COUNT);

    }

}
