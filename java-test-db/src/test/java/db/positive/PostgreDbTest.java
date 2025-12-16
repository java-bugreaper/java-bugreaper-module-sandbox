package db.positive;

import db.TestsSetupDB;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;


@Feature("Db checks: PostgreSQL")
class PostgreDbTest extends TestsSetupDB {


    @Test
    @Description("""
            Create data in Postgre table
            -> check data in Postgre table""")
    void checkPostgreDb_fixtureCreate_dataExistCheck() {

        final var id = 2;
        final var role = "user";

        final var name = "Miha";
        final var email = "miha@com.com";

        sqlPgInsert.insertIntoRoles(new Object[][]{
                {"role_id", id},
                {"role_name", role},
        });

        sqlPgInsert.insertIntoAccounts(new Object[][]{
                {"user_id", 1},
                {"username", name},
                {"password", "pass-123"},
                {"email", email},
                {"fee", 55.99},
        });

        sqlPgAssert.assertRoles(1);

        sqlPgAssert.recordRoles_exist(new Object[][]{
                {"role_id", id},
                {"role_name", role},
        });

        sqlPgAssert.recordAccounts_exist(new Object[][]{
                {"username", name},
        });
    }

    @Test
    @Description("""
            Create date data in Postgre table
            -> check date data in Postgre table""")
    void checkPostgreDb_fixtureCreateDates_dataExistCheck() {

        final var name = "Miha";
        final var email = "miha@com.com";


        sqlPgInsert.insertIntoAccounts(new Object[][]{
                {"user_id", 1},
                {"username", name},
                {"email", email},
                {"fee", null},
                {"is_active", true},
                {"created_on", DATE_TIME_TEST},
                {"last_login", null},
        });

        sqlPgAssert.recordAccounts_exist(new Object[][]{
                {"username", name},
                {"created_on", ">=", DATE_TEST},
                {"is_active", true},
                {"last_login", null},
        });
    }

    @Test
    @Description("""
            check data not exist""")
    void checkPostgreDb_dataNotExistCheck() {

        sqlPgAssert.recordAccounts_notExist();
        sqlPgAssert.assertRoles(0);
    }

    @Test
    @Description("""
            check data exist" in dump""")
    void checkPostgreDb_existInDump() {

        sqlPgAssert.iSeeInCityDump(new Object[][]{
                {"name", "Kyiv"},
                {"is_capital", true},
                {"created_on", ">=", "2000-01-01"},
                {"created_on", "<=", "2099-12-12"},
        });

    }

}
