import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import model.Song;
import mongodb.SongManagement;
import mongodb.MongoDbUtil;

import static com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb.MongoServerRuleBuilder.newManagedMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;

public class AddASong {
    // nếu muốn chạy một MongoDB in‐memory hoặc managed mongod cục bộ:
    @ClassRule
    public static ManagedMongoDb managedMongod = newManagedMongoDbRule()
            .mongodPath("C:\\Program Files\\MongoDB\\Server\\8.0")
            .appendSingleCommandLineArguments("-vvv")
            .build();

    // rule mặc định sẽ xóa sạch db "testdb" trước mỗi @Test
    @Rule
    public MongoDbRule remoteMongoDbRule = newMongoDbRule()
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
            locations = { "/initialSongs.json" },
            loadStrategy = LoadStrategyEnum.CLEAN_INSERT
    )
    @ShouldMatchDataSet(
            location = "/expectedSongs.json"
    )
    public void createANewSong() {
        SongManagement songManager = new SongManagement(MongoDbUtil.getCollection("song"));
        Song s1 = new Song("Let It Be", "The Beatles");
        Song s2 = new Song("Billie Jean", "Michael Jackson");
        songManager.save(s1);
        songManager.save(s2);
    }
}
