package db;


import io.bugreaper.modules.db.MsSQL;
import io.qameta.allure.Step;


public class SqlMs {


    private static final String SCHEMA = "test_schema";

    private final MsSQL jdbc;

    private static SqlMs INSTANCE;

    private SqlMs() {
        jdbc = new MsSQL();

        // provide dump
        jdbc.runScriptFromFile("dump/mssql_setup.sql");
    }

    public static SqlMs getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SqlMs();
        }

        return INSTANCE;
    }

    //  POST

    public void insertIntoDrivers(Object[][] testArray) {

        final String tableName = SCHEMA + ".drivers";
        final Object[][] TEMPLATE = {
                {"id", 1},
                {"full_name", "Miha"},
                {"fee", 55.15},
                {"birth", "1999-01-01"},
                {"last_call", "2023-01-12 17:00:00"},
        };
        jdbc.insertIntoTable(tableName, TEMPLATE, testArray);
    }

    @Step("Clean MsSql tables")
    public void cleanMsSqlDb() {
        jdbc.truncateTable(SCHEMA + ".drivers");
    }

    // GET
    
    public void assertDrivers(int expectedCount) {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".drivers", expectedCount);
    }

    public void recordDrivers_notExist() {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".drivers", 0);
    }

    public void recordDrivers_exist(Object[][] conditionsArray) {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".drivers",
                        conditionsArray, 1);
    }
    
    public void iSeeInTable(String tableName) {
        jdbc.showDataFromTable(tableName);
    }

}
