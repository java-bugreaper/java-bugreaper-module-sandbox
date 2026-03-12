package db.positive;

import db.TestsSetupDB;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


@Feature("Db checks: OracleDb")
class OracleDbTest extends TestsSetupDB {


    @Test
    @Description("""
            Create data in OracleDb table
            with three elements (timestamp column)
            -> check created records with conditions (DateTime Not supported!)""")
    void checkOracleDb_fixtureCreate() {

        sqlOracle.insertIntoGoods(new Object[][]{
                {"CATEGORY_ID", 1556},
                {"NAME", "MAIN category"},
                {"UPDATE_DATE", DATE_TIME_TEST2, "TIMESTAMP"},
        });

        sqlOracle.recordGoods_exist(new Object[][]{
                {"CATEGORY_ID", 1556},
                {"NAME", "MAIN category"},
        });
    }

    @Test
    @Description("""
            Create data in OracleDb table by json-provider
            with three elements (date column)
            -> check created records with conditions (json-provider)""")
    void checkOracleDb_jsonTemplateMergeAndAssert() {

        sqlOracle.insertIntoGoodsV2(
                """
                        {
                          "CATEGORY_ID": 1556,
                          "NAME": "MAIN category",
                          "PRICE": 299.99,
                          "CREATE_DATE:DATE": "2025-05-05"
                        }
                        """);

        sqlOracle.recordGoods_existV2(
                """
                        {
                          "CATEGORY_ID": 1556,
                          "NAME": "MAIN category",
                          "PRICE": 299.99,
                          "CREATE_DATE:=:DATE": "2025-05-05"
                        }
                        """);
    }

    @Test
    @Description("""
            Create data in OracleDb table several records
            -> check count of created records without conditions""")
    void checkOracleDb_fixtureCreateTwo() {

        sqlOracle.insertIntoGoods(new Object[][]{
                {"ID", 101},
                {"CATEGORY_ID", 1},
                {"NAME", "first category"},
                {"UPDATE_DATE", DATE_TIME_TEST, "TIMESTAMP"},
        });

        sqlOracle.insertIntoGoods(new Object[][]{
                {"ID", 102},
                {"CATEGORY_ID", 2},
                {"PRICE", 999.99},
                {"NAME", "second category"},
        });

        sqlOracle.assertCountFromGoods(2);
    }

    @Test
    @Description("""
            No data in table
            cleaned by 'Before Each'""")
    void checkOracleDb_cleaned() {

        sqlOracle.recordGoods_notExist();
    }

    @Test
    @Description("""
            Check data in Oracle table (dump) with all columns info""")
    void checkOracleDb_checkDumpExist() {

        sqlOracle.iSeeInCitiesDump(new Object[][]{
                {"NAME", "Kyiv"},
                {"CAPITAL", 1},
        });
    }

    @Test
    @Description("""
            Check data in Oracle table (by date condition)""")
    void checkOracleDb_checkDateTypeExist() {
        sqlOracle.iSeeInCitiesDump(new Object[][]{
                {"CREATE_DATE", ">", "DATE", "1999-07-07"},
        });
    }

    @Test
    @Description("""
            Check data in Oracle table (by date condition)""")
    void checkOracleDb_checkDateTypeNullExist() {
        sqlOracle.iSeeInCitiesDump(new Object[][]{
                {"CREATE_DATE", null},
        });
    }

    @Test
    @Description("""
            Create date field with DateTime
            Check data in Oracle table (by date condition)""")
    void checkOracleDb_checkDateTimeTypeExist() {

        sqlOracle.insertIntoGoods(new Object[][]{
                {"CATEGORY_ID", 1},
                {"NAME", "first category"},
                {"UPDATE_DATE", "2022-07-18 22:00:00"},
        });

        sqlOracle.recordGoods_exist(new Object[][]{
                {"NAME", "first category"},
                {"UPDATE_DATE", ">=", "TIMESTAMP", "2022-07-18 11:00:00"},
        });
    }

    @Test
    @Description("""
            Create date field with Date where template is Datetime
            Check data in Oracle table (by date condition)""")
    void checkOracleDb_checkDateKeyTypeExist() {

        sqlOracle.insertIntoGoods(new Object[][]{
                {"CATEGORY_ID", 1},
                {"NAME", "first category"},
                {"UPDATE_DATE", "2022-07-18", "DATE"},
        });

        sqlOracle.recordGoods_exist(new Object[][]{
                {"NAME", "first category"},
                {"UPDATE_DATE", "=", "DATE", "2022-07-18"},
        });
    }

    @Test
    @Description("""
            Check report when assert failed""")
    void checkFAILED_onAssert() {

        sqlOracle.insertIntoGoods(new Object[][]{
                {"CATEGORY_ID", 1},
                {"NAME", "first category"},
                {"UPDATE_DATE", "2022-07-18", "DATE"},
        });


        assertThrows(AssertionError.class, () ->
                sqlOracle.recordGoods_exist(new Object[][]{
                        {"NAME", "first category"},
                        {"UPDATE_DATE", "=", "DATE", "2011-11-11"},
                }));

    }

}
