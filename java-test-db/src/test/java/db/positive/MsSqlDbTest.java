package db.positive;

import db.TestsSetupDB;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;


@Feature("Db checks: MsSql")
class MsSqlDbTest extends TestsSetupDB {


    @Test
    @Description("""
            Create data in MsSql table
            -> check data in MsSql table""")
    void checkMsSql_fixtureCreate_dataExistCheck() {

        final var id = 2;
        final var name = "John";
        final var fee = 999.99;

        msSql.insertIntoDrivers(new Object[][]{
                {"id", id},
                {"full_name", name},
                {"fee", fee},
        });

        msSql.assertDrivers(1);

        msSql.recordDrivers_exist(new Object[][]{
                {"id", id},
                {"full_name", name},
        });
    }

    @Test
    @Description("""
           Check empty data in MsSql table""")
    void checkMsSql_emptyTable() {

        msSql.assertDrivers(0);

        msSql.iSeeInTable("test_schema.drivers");
    }

    @Test
    @Description("""
            Create data in MsSql table with Null
            -> check data in MsSql table""")
    void checkMsSql_fixtureCreateWithNull_dataExistCheck() {

        final var id = 2;

        msSql.insertIntoDrivers(new Object[][]{
                {"id", id},
                {"full_name", ""},
                {"fee", null},
        });

        msSql.recordDrivers_exist(new Object[][]{
                {"id", id},
                {"full_name", ""},
                {"fee", null},
        });
    }

    @Test
    @Description("""
            Create data in MsSql table with dates & Times
            -> check data in MsSql table""")
    void checkMsSql_fixtureCreateWithDates_dataExistCheck() {

        final var id = 2;
        final var name = "John";

        msSql.insertIntoDrivers(new Object[][]{
                {"id", id},
                {"full_name", name},
                {"birth", DATE_TEST},
                {"last_call", DATE_TIME_TEST},
        });

        msSql.recordDrivers_exist(new Object[][]{
                {"id", id},
                {"full_name", name},
                {"birth", DATE_TEST},
                {"last_call", DATE_TIME_TEST},
        });
    }

    @Test
    @Description("""
            Create data in MsSql table with diffConditions
            -> check data in MsSql table""")
    void checkMsSql_fixtureCreateWithConditions_dataExistCheck() {

        msSql.insertIntoDrivers(new Object[][]{
                {"full_name", "Oleksander"},
                {"birth", "2044-06-06"},
                {"last_call", "2020-01-01"},
        });

        msSql.assertDrivers(1);

        msSql.recordDrivers_exist(new Object[][]{
                {"full_name", "LIKE", "%sander%"},
                {"birth", ">=", "2020-01-01"},
                {"birth", "<", "2099-01-01"},
                {"last_call", "IS NOT", null},
        });
    }

}
