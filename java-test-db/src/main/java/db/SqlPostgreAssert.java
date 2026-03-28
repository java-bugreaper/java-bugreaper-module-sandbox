package db;


import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.modules.db.PostgreSQL;

import java.util.TimeZone;

public class SqlPostgreAssert {


    private static final String SCHEMA = YamlUtils.getStringValueByPath("modules.db.postgresql.schema");

    private final PostgreSQL jdbc;

//    private static SqlPostgreAssert INSTANCE;

    public SqlPostgreAssert() {
        jdbc = PostgreSQL.getInstance();
    }

//    public static SqlPostgreAssert getInstance() {
//        if (INSTANCE == null) {
//            // temp costil for local run ->  invalid value for parameter "TimeZone": "Europe/Kyiv"
//            //TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kiev"));
//            // no problem after postgres 15+
//            INSTANCE = new SqlPostgreAssert();
//        }
//
//        return INSTANCE;
//    }

    // GET

    public void assertRoles(int expectedCount) {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".roles", expectedCount);
    }

    public void recordRoles_notExist() {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".roles", 0);
    }

    public void recordRoles_exist(Object[][] conditionsArray) {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".roles",
                        conditionsArray, 1);
    }

    public void assertAccounts(int expectedCount) {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".accounts", expectedCount);
    }

    public void recordAccounts_notExist() {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".accounts", 0);
    }

    public void recordAccounts_exist(Object[][] conditionsArray) {
        jdbc.seeRowsCountInTableExactly
                (SCHEMA + ".accounts",
                        conditionsArray, 1);
    }


    public void iSeeInTable(String tableName) {
        jdbc.showDataFromTable(tableName);
    }

    public void iSeeInCityDump(Object[][] condition) {
        jdbc.showDataByCondition("public.cities_dict", condition);
    }

}
