import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import model.Song;
import mongodb.SongManagement;
import mongodb.MongoDbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb.MongoServerRuleBuilder.newManagedMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;

public class FindSongByArtist {

    @ClassRule
    public static ManagedMongoDb managedMongod = newManagedMongoDbRule()
            .mongodPath("C:\\Program Files\\MongoDB\\Server\\8.0")
            .appendSingleCommandLineArguments("-vvv")
            .build();

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule()
            .defaultManagedMongoDb("test");

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

    private SongManagement manager;

    @Before
    public void setUp() {
        MongoCollection<Document> collection = MongoDbUtil.getCollection("song");
        collection.drop();

        String[] others = {"The Beatles","Taylor Swift","Eminem","Adele","Coldplay"};
        Random rnd = new Random();
        List<Document> docs = new ArrayList<>(10000);

        for (int i = 0; i < 10000; i++) {
            String artist = (i < 1000) ? "Michael Jason"
                    : others[rnd.nextInt(others.length)];
            docs.add(new Document("title", "Song " + i)
                    .append("artist", artist));
        }
        collection.insertMany(docs);

        manager = new SongManagement(collection);
    }

    @Test
    public void findSongsByArtist() {
        List<Song> result = manager.findByArtist("Michael Jason");

        // 1.000 dòng phải đúng artist này
        assertEquals(1000, result.size());
        // Tất cả đều có artist là “Michael Jason”
        result.forEach(song ->
                assertEquals("Michael Jason", song.getArtist())
        );
    }
}
