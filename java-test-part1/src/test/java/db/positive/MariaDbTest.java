package db.positive;

import db.TestsSetupDB;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;


@Feature("Db checks: MariaDb")
class MariaDbTest extends TestsSetupDB {


    @Test
    @Description("""
            Create data in MariaDb table
            -> check data in MariaDb table""")
    void checkMariaDb_fixtureCreate_dataExistCheck_links() {

        final var id = 15;
        final var name = "Alex";
        final var email = "alex@com.com";
        final var amount = 11.99;

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
                {"EMAIL", email},
                {"STATE", true},
                {"AMOUNT", amount},
                {"DESCRIPTION", null},
        });


        sqlMaria.recordUsers_exist(new Object[][]{
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
            Create JSON data in MariaDb table
            -> check JSON data in MariaDb table like text (Don`t use it)""")
    void checkMariaDb_jsonDataCheck_likeText() {

        final var id1 = 15;

        final var id2 = 20;
        final var json2 = """
                {"id":1,"text":"test"}""";

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id1},
                {"USERNAME", "Alex"},
                {"DATA", "[]"},
        });
        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id2},
                {"USERNAME", "Anna"},
                {"DATA", json2},
        });


        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", id1},
                {"DATA", "[]"},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", id2},
                {"DATA", json2},
        });
    }

    @Test
    @Description("""
            Create JSON data in MariaDb table
            -> check JSON data in MariaDb table JSON contains""")
    void checkMariaDb_jsonDataCheck_jsonContains() {

        final var id2 = 20;

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id2},
                {"DATA", """
                        {"id":1,"text":"test"}"""},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", id2},
                {"DATA", "JSON_CONTAINS", """
                    {"id":1}"""},
        });
    }

    @Test
    @Description("""
            Create JSON array data in MariaDb table
            -> check JSON data in MariaDb table JSON contains""")
    void checkMariaDb_jsonArrayDataCheck_jsonContains() {

        final var id2 = 20;

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id2},
                {"DATA", """
                        [{"id":1,"text":"test"}, {"id":2,"text":"test2"}]"""},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", id2},
                {"DATA", "JSON_CONTAINS", """
                    [{"id":1}]"""},
        });
    }

    @Test
    @Description("""
            Create JSON array data in MariaDb table
            -> check JSON data in MariaDb table JSON equals""")
    void checkMariaDb_jsonArrayDataCheck_jsonEquals() {

        final var id2 = 20;

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id2},
                {"DATA", """
                        [{"id":1,"text":"test"}, {"id":2,"text":"test2"}]"""},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", id2},
                {"DATA", "JSON_EQUALS", """
                    [{"id":1,"text":"test"}, {"id":2,"text":"test2"}]"""},
        });
    }

    @Test
    @Description("""
            Create data in MariaDb table
            -> check Date data match regex""")
    void checkMariaDb_regexDateDataCheck() {


        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", "1"},
                {"BIRTHDAY", "1986-05-01"},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", "1"},
                {"BIRTHDAY", "REGEX_MATCH", "[0-9]{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])"},
        });
    }

    @Test
    @Description("""
            Create data in MariaDb table (2 records)
            -> check records rows in MariaDb table""")
    void checkMariaDb_fixtureCreate_dataCountCheck() {

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", "1"},
        });
        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", "2"},
                {"USERNAME", "Mark"},
        });

        sqlMaria.assertCountFromUsers(2);
    }


    @Test
    @Description("""
            Create data with DateTime in MariaDb table
            -> check data in MariaDb table with three elements""")
    void checkMariaDb_fixtureDateTimeCreate_dataExistWith3ElementsCheck() {

        final var name = "Alex";

        sqlMaria.insertIntoUsers(new Object[][]{
                {"USERNAME", name},
                {"CREATE_DATE", TEST_20_MINUTES},
                {"UPDATE_DATE", TEST_10_MINUTES},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"USERNAME", name},
                {"CREATE_DATE", TEST_20_MINUTES},
                {"CREATE_DATE", ">=", TEST_10_MINUTES},
                {"UPDATE_DATE", "<", TEST_30_MINUTES},
        });

    }

    @Test
    @Description("""
            Create data in MariaDb table
            -> check data by hardcode condition""")
    void checkMariaDb_fixture_dataExistWithHardcodeConditionCheck() {

        final var name = "Alex";

        sqlMaria.insertIntoUsers(new Object[][]{
                {"USERNAME", name},
                {"DEPOSIT", "55.00"},
        });

        sqlMaria.recordUsers_exist("where ID >=1 OR DEPOSIT > 10");
    }

    @Test
    @Description("""
            Check data in MariaDb table (dump) with all columns info""")
    void checkMariaDb_checkDumpExist() {

        sqlMaria.iSeeInCategoryDump(new Object[][]{
                {"NAME", "SSD"},
                {"STATE", true},
        });

        sqlMaria.iSeeInCategoryDump(new Object[][]{
                {"NAME", "Monitors"},
                {"ACTIVE_DATE", "<", "2025-12-12"},
                {"ACTIVE_DATE", ">=", "1999-01-01 00:00:00"},
        });

    }

    @Test
    @Description("""
            Delete data in MariaDb table by conditions
            -> check data in MariaDb table""")
    void checkMariaDb_deleteByConditions() {

        final var id = "15";
        final var name = "Alex";

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
        });
        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", "2"},
                {"USERNAME", "dummy"},
        });

        sqlMaria.recordUsers_delete(
                new Object[][]{
                        {"ID", id},
                        {"USERNAME", name},
                }
        );

        sqlMaria.recordUsers_notExist(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
        });

        sqlMaria.recordUsers_exist(new Object[][]{
                {"ID", "2"},
                {"USERNAME", "dummy"},
        });

    }

    @Test
    @Description("""
            Update data in MariaDb table by conditions
            -> check data in MariaDb table""")
    void checkMariaDb_updateByConditions() {

        final var id = 15;
        final var name = "Alex";
        final var email = "alex@com.com";

        sqlMaria.insertIntoUsers(new Object[][]{
                {"ID", id},
                {"USERNAME", name},
                {"EMAIL", email},
        });

        sqlMaria.recordUsers_update(
                new Object[][]{
                        {"USERNAME", name},
                        {"EMAIL", email},
                },
                new Object[][]{
                        {"ID", id},
                }
        );

        sqlMaria.recordUsers_exist(new Object[][]{
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

                sqlMaria.insertIntoUsers(new Object[][]{
                        {"ID", String.valueOf(i)},
                        {"USERNAME", "Alex_" + i},
                        {"EMAIL", "alex_" + i + "@com.com"},
                });
            }
        });

        sqlMaria.assertCountFromUsers(RECORDS_COUNT);

    }

}
