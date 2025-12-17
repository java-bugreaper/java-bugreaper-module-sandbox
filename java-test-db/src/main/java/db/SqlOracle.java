package db;


import net.bugreaper.modules.db.OracleDb;
import io.qameta.allure.Step;


public class SqlOracle {


    private static SqlOracle INSTANCE;
    private final OracleDb jdbcOracle;

    private SqlOracle() {
        jdbcOracle = new OracleDb();

    }


    public static SqlOracle getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SqlOracle();
        }

        return INSTANCE;
    }

    //  POST

    public void insertIntoGoods(Object[][] testArray) {

        final String tableName = "ORACLE_SCHEMA.GOODS";
        final Object[][] TEMPLATE = {
                {"ID", 200},
                {"CATEGORY_ID", 1},
                {"NAME", "dummy_category"},
                {"PRICE", 175.99},
                {"CREATE_DATE", "2020-01-01 10:00:00", "TIMESTAMP"},
                {"UPDATE_DATE", "2020-01-01 11:00:00", "TIMESTAMP"},
        };
        jdbcOracle.insertIntoTable(tableName, TEMPLATE, testArray);
    }

    //new JSON template feature
    public void insertIntoGoodsV2(String jsonProvider) {
        jdbcOracle.insertIntoTable("ORACLE_SCHEMA.GOODS", jsonProvider);
    }

    @Step("Clean Oracle tables")
    public void cleanOracleDb() {
        jdbcOracle.truncateTable("ORACLE_SCHEMA.GOODS");
    }

    // GET

    public void recordGoods_exist(Object[][] conditionsArray) {
        jdbcOracle.seeRecordExistsInTable
                ("ORACLE_SCHEMA.GOODS", conditionsArray);
    }

    public void recordGoods_existV2(String jsonProvider) {
        jdbcOracle.seeRecordExistsInTable
                ("ORACLE_SCHEMA.GOODS", jsonProvider);
    }

    public void assertCountFromGoods(int expectedCount) {
        jdbcOracle.seeRowsCountInTableExactly
                ("ORACLE_SCHEMA.GOODS", expectedCount);
    }

    public void recordGoods_notExist() {
        jdbcOracle.seeTableIsEmpty("ORACLE_SCHEMA.GOODS");
    }

    public void iSeeInTable(String tableName) {
        jdbcOracle.showDataFromTable(tableName);
    }

    public void iSeeInCitiesDump(Object[][] condition) {
        jdbcOracle.showDataByCondition("ORACLE_SCHEMA.CITIES_DICT", condition);
    }

}
