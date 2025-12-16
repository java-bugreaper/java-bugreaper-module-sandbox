package db;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("db")
@Epic("[info] Info")
final public class InfoTest {

    private final SqlPostgreAssert sqlPgAssert = SqlPostgreAssert.getInstance();
    private final SqlMs msSql = SqlMs.getInstance();
    private final SqlOracle sqlOracle = SqlOracle.getInstance();


    @Test
    @DisplayName("Dump Postgre Info")
    void dumpPostgreInfo() {
        sqlPgAssert.iSeeInTable("public.cities_dict");
    }

    @Test
    @DisplayName("Dump MsSql Info")
    void dumpMsSqlInfo() {
        msSql.iSeeInTable("test_schema.drivers");
    }

    @Test
    @DisplayName("Dump Oracle Info")
    void dumpOracleInfo() {
        sqlOracle.iSeeInTable("ORACLE_SCHEMA.CITIES_DICT");
        sqlOracle.iSeeInTable("ORACLE_SCHEMA_2.COUNTRIES");
    }

}
