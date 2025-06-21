import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import model.Song;
import mongodb.SongManagement;
import mongodb.MongoDbUtil;

import java.util.List;

import static org.junit.Assert.*;
import static com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb.MongoServerRuleBuilder.newManagedMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;

public class FindAllSongs {

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
    public void shouldFindAllSongs() {
        SongManagement songManager = new SongManagement(MongoDbUtil.getCollection("song"));
        List<Song> allSongs = songManager.findAll();

        // Assert số lượng bài hát tìm được đúng
        assertEquals("Expected 3 songs in database", 3, allSongs.size());

        // In ra để kiểm tra trực quan
        System.out.println("📀 Found songs:");
        allSongs.forEach(System.out::println);
    }
}
