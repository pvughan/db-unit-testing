import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

import model.Song;
import mongodb.SongManagement;
import mongodb.MongoDbUtil;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb.MongoServerRuleBuilder.newManagedMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.*;

public class FindSongByIndex {

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

    @Test
    @UsingDataSet(
            locations = "/song.json",
            loadStrategy = LoadStrategyEnum.CLEAN_INSERT
    )
    public void findSongById() {
        SongManagement songManager = new SongManagement(MongoDbUtil.getCollection("song"));

        String targetId = "666a1f2e95c62162b8d1e001";  // ID phải giống với song.json
        Song result = songManager.findById(targetId);

        assertNotNull(result);
        assertEquals("Let It Be", result.getTitle());
        assertEquals("Obama", result.getArtist());
    }
}