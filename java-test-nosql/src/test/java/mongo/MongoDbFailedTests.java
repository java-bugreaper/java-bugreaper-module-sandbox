package mongo;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import net.bugreaper.modules.mongodb.MongoDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


@Tag("nosql")
@Epic("[mongodb-failed] Expected failed MongoDb tests")
class MongoDbFailedTests {


    MongoDb mongo = MongoDb.getInstance();
    private static final String COLLECTION = "test-collection";

    @BeforeEach
    void clean() {
        mongo.cleanCollection(COLLECTION);
    }


    @Test
    @Description("""
            Check records not existing await FAILED""")
    void notEmptyFailedTest() {

        mongo.insertIntoCollection(
                COLLECTION,
                """
                        {
                            name: 'Alex'
                        }"""
        );

        assertThrows(AssertionError.class, () ->
                mongo.seeCollectionIsEmpty(COLLECTION));
    }

    @Test
    @Description("""
            Create record with array
            -> check FAILED STRICT assert for record existing""")
    void insertAndStrictAssertsRecordArraysFailedTest() {

        mongo.cleanCollection(COLLECTION);

        mongo.insertIntoCollection(
                COLLECTION,
                """
                        {
                             "user": {
                                "name": "Alex",
                                "age": 19,
                                "array": ["25", "26", "27"],
                                "array2": ["21", "22", "23"],
                                "extra_field": "test"
                            }
                        }""");

        assertThrows(AssertionError.class, () ->
                mongo.seeRecordExistsInCollection(
                        COLLECTION,
                        """
                                {
                                     "user": {
                                        "name": "Alex",
                                        "age": 19,
                                        "array": ["27", "26", "25"],
                                        "array2": ["21", "22",]
                                    }
                                }"""));
    }


    @Test
    @Description("""
            Create record with array
            -> check FAILED CONTAINS assert for record existing""")
    void insertAndContainsAssertsRecordArraysFailedTest() {

        mongo.cleanCollection(COLLECTION);

        mongo.insertIntoCollection(
                COLLECTION,
                """
                        {
                             "user": {
                                "name": "Alex",
                                "age": 19,
                                "array": ["25", "26", "27"]
                            }
                        }""");

        assertThrows(AssertionError.class, () ->
                mongo.seeRecordPartExistsInCollection(
                        COLLECTION,
                        """
                                {
                                     "user": {
                                        "name": "Alex2",
                                        "age": null,
                                        "array": ["27","28"]
                                    }
                                }"""));

    }

}
