package setup.db;


import net.bugreaper.modules.db.MariaDb;
import io.qameta.allure.Step;


public class SqlMaria {

    private final String SCHEMA = "MARIA_SCHEMA";

    private final MariaDb jdbc;

    private static SqlMaria INSTANCE;

    private SqlMaria() {
        jdbc = new MariaDb();
    }

    // Good practice to use Singleton
    // it`s just reuse created object
    // and don`t create multiple objects - every with own hikariCP pools
    public static SqlMaria getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SqlMaria();
        }

        return INSTANCE;
    }

    //  POST

    public void insertIntoUsers(Object[][] testArray) {

        final String tableName = SCHEMA + ".USERS";
        final Object[][] TEMPLATE = {
                {"ID", 100},
                {"DEPOSIT", null},
                {"USERNAME", "Pupkin"},
                {"PASSWORD", "pupKin1992"},
                {"EMAIL", "pupkin@com.com"},
                {"STATE", false},
                {"IS_ACTIVE", false},
                {"DESCRIPTION", "dummy description"},
                {"ROLES", "manager"},
                {"AMOUNT", 10.55},
                {"DATA", "[]"},
                {"BIRTHDAY", "1999-01-01"},
                {"CREATE_DATE", "2021-02-02 10:00:00"},
                {"UPDATE_DATE", "2021-02-02 10:00:00"},
        };

        jdbc.insertIntoTable(tableName, TEMPLATE, testArray);
    }

    public void recordUsers_update(Object[][] setArray,
                                   Object[][] conditionsArray) {
        jdbc.updateTable(SCHEMA + ".USERS", setArray, conditionsArray);
    }

    public void recordUsers_delete(Object[][] conditionsArray) {
        jdbc.deleteFromTable(SCHEMA + ".USERS", conditionsArray);
    }

    @Step("Clean MariaDB tables")
    public void cleanMariaDb() {
        jdbc.truncateTable(SCHEMA + ".USERS");
    }

    // GET

    public void assertCountFromUsers(int expectedCount) {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".USERS", expectedCount);
    }

    public void recordUsers_notExist() {
        jdbc.seeTableIsEmpty(SCHEMA + ".USERS");
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

    public void iSeeInTable(String tableName) {
        jdbc.showDataFromTable(tableName);
    }

    public void iSeeInCategoryDump(Object[][] condition) {
        jdbc.showDataByCondition("MARIA_SCHEMA.CATEGORY_DICT", condition);
    }


}
