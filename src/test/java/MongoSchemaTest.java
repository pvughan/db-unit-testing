import com.mongodb.client.*;
import org.bson.Document;
import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MongoSchemaTest {

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> songs;

    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            System.out.println("✅ " + description.getMethodName() + " passed.");
        }

        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println("❌ " + description.getMethodName() + " failed: " + e.getMessage());
        }
    };

    @BeforeClass
    public static void connectToDb() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("test");
        songs = database.getCollection("song");
    }

    @AfterClass
    public static void closeConnection() {
        mongoClient.close();
    }

    @Test
    public void testCollectionExists() {
        List<String> collections = database.listCollectionNames().into(new ArrayList<>());
        assertTrue("Collection 'song' must exist", collections.contains("song"));
    }

    @Test
    public void testAllDocumentsHaveValidStructureAndValues() {
        FindIterable<Document> documents = songs.find();
        int count = 0;

        for (Document song : documents) {
            count++;

            assertTrue("Missing field: title", song.containsKey("title"));
            assertTrue("Missing field: artist", song.containsKey("artist"));
            assertTrue("Missing field: year", song.containsKey("year"));

            Object titleObj = song.get("title");
            Object artistObj = song.get("artist");
            Object yearObj = song.get("year");

            assertTrue("title must be a String", titleObj instanceof String);
            assertTrue("artist must be a String", artistObj instanceof String);
            assertTrue("year must be an Integer", yearObj instanceof Integer);

            String title = ((String) titleObj).trim();
            String artist = ((String) artistObj).trim();
            int year = (Integer) yearObj;

            assertFalse("Title should not be blank", title.isEmpty());
            assertTrue("Title max 100 chars", title.length() <= 100);

            assertFalse("Artist should not be blank", artist.isEmpty());
            assertTrue("Artist max 100 chars", artist.length() <= 100);

            assertTrue("Year must be >= 1900", year >= 1900);
            assertTrue("Year must be <= 2025", year <= 2025);
        }

        assertTrue("No documents found in 'song' collection", count > 0);
    }
}
