package db;


import net.bugreaper.core.config.YamlUtils;
import net.bugreaper.modules.db.PostgreSQL;
import io.qameta.allure.Step;

import java.util.TimeZone;


public class SqlPostgreInsert {

    private static final String SCHEMA = YamlUtils.getStringValueByPath("modules.db.postgresql.schema");

    private final PostgreSQL jdbc;

    private static SqlPostgreInsert INSTANCE;

    private SqlPostgreInsert() {
        // temp costil for local run ->  invalid value for parameter "TimeZone": "Europe/Kyiv"
        //TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kiev"));
        // no problem after postgres 15+
        jdbc = new PostgreSQL();
    }

    public static SqlPostgreInsert getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SqlPostgreInsert();
        }

        return INSTANCE;
    }

    //  POST

    public void insertIntoRoles(Object[][] testArray) {

        final String tableName = SCHEMA + ".roles";
        //example with Object template
        final Object[][] TEMPLATE = {
                {"role_id", 1},
                {"role_name", "admin"},
        };

        jdbc.insertIntoTable(tableName, TEMPLATE, testArray);
    }

    public void insertIntoAccounts(Object[][] testArray) {

        //example with json-file  template
        jdbc.insertIntoTable(SCHEMA + ".accounts", testArray);
    }

    @Step("Clean Postgre tables")
    public void cleanPostgreDb() {
        jdbc.truncateTableCascade(SCHEMA + ".accounts");
        jdbc.truncateTableCascade(SCHEMA + ".roles");
    }

}
