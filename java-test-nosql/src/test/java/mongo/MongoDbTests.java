package mongo;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import net.bugreaper.modules.mongodb.MongoDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("nosql")
@Epic("[mongodb] Tests for MongoDb")
class MongoDbTests {


    MongoDb mongo = MongoDb.getInstance();
    private static final String COLLECTION = "users";
    
    @BeforeEach
    void clean(){
        mongo.cleanCollection(COLLECTION);
    }


    @Test
    @Description("""
            Create/Clean data in MsMongoDb collection
            -> check records count and existing""")
    void emptyNotEmptyTest(){

        mongo.seeCollectionIsEmpty(COLLECTION);
        mongo.seeRecordsCountInCollectionExactly(COLLECTION, 0);

        mongo.insertIntoCollection(
                COLLECTION,
                """
                        {
                            name: 'Alex'
                        }"""
        );

        mongo.seeCollectionIsNotEmpty(COLLECTION);
        mongo.seeRecordsCountInCollectionExactly(COLLECTION, 1);
    }

    @Test
    @Description("""
            Create record with array
            -> check STRICT assert for record existing""")
    void insertAndStrictAssertsRecordArraysTest() {

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

        mongo.seeRecordExistsInCollection(
                COLLECTION,
                """
                        {
                             "user": {
                                "name": "Alex",
                                "age": 19,
                                "array": ["25", "26", "27"]
                            }
                        }""");

    }


    @Test
    @Description("""
            Create record with array
            -> check CONTAINS assert for record existing""")
    void insertAndContainsAssertsRecordArraysTest() {

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

        mongo.seeRecordPartExistsInCollection(
                COLLECTION,
                """
                        {
                             "user": {
                                "name": "Alex",
                                "array": ["27","25"]
                            }
                        }""");

    }

    @Test
    @Description("""
            Create records
            -> check by grabDocumentsFromCollection""")
    void insertAndGrabTest() {

        mongo.insertIntoCollection(
                COLLECTION,
                """
                        {
                            "name": "Alex",
                            "age": 19
                        }""");

        mongo.insertIntoCollection(
                COLLECTION,
                """
                        {
                            "name": "John",
                            "age": 20
                        }""");

        mongo.grabDocumentsFromCollection(COLLECTION)
                .seeListAnyContainsJson("""
                    {
            
                      "name": "Alex"
                    }""")
                .seeListAnyContainsJson("""
                    {
            
                      "age": 20
                    }""")
                .seeListHasExactlyCount(2);

    }

}
