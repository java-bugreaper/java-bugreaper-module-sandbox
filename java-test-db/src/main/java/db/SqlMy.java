package db;


import net.bugreaper.modules.db.MySQL;
import io.qameta.allure.Step;


public class SqlMy {

    private final MySQL jdbc;

    private static SqlMy INSTANCE;

    private SqlMy() {
        jdbc = new MySQL();
    }

    // Good practice to use Singleton
    // it`s just reuse created object
    // and don`t create multiple objects - every with own hikariCP pools
    public static SqlMy getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SqlMy();
        }

        return INSTANCE;
    }


    private final String SCHEMA = "MYSQL_SCHEMA";

    //  POST

    public void insertIntoUsers(Object[][] testArray) {

        jdbc.insertIntoTable(SCHEMA + ".USERS", testArray);
    }

    public void recordUsers_update(Object[][] setArray,
                                   Object[][] conditionsArray) {
        jdbc.updateTable(SCHEMA + ".USERS", setArray, conditionsArray);
    }

    public void recordUsers_delete(Object[][] conditionsArray) {
        jdbc.deleteFromTable(SCHEMA + ".USERS", conditionsArray);
    }

    @Step("Clean MySql tables")
    public void cleanMySql() {
        jdbc.truncateTable(SCHEMA + ".USERS");
    }

    // GET

    public void assertCountFromUsers(int expectedCount) {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".USERS", expectedCount);
    }

    public void recordUsers_notExist(Object[][] conditionsArray) {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".USERS", conditionsArray, 0);
    }

    public void recordUsers_exist(Object[][] conditionsArray) {
        jdbc.seeRecordExistsInTable(SCHEMA + ".USERS", conditionsArray);
    }

    public void recordUsers_exist(String conditionsString) {
        jdbc.seeRecordExistsInTableCustom
                (SCHEMA + ".USERS", conditionsString);
    }

    public void iSeeInTableUsers() {
        jdbc.showDataFromTable(SCHEMA + ".USERS");
    }

    public void iSeeInCategoryDump(Object[][] condition) {
        jdbc.showDataByCondition("MYSQL_SCHEMA.CATEGORY_DICT", condition);
    }


}
