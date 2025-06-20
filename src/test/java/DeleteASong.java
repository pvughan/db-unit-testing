import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;

import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import mongodb.SongManagement;
import mongodb.MongoDbUtil;

import static com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb.MongoServerRuleBuilder.newManagedMongoDbRule;
import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;

public class DeleteASong {

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
            locations = "/initialSongs.json",
            loadStrategy = LoadStrategyEnum.CLEAN_INSERT
    )
    @ShouldMatchDataSet(
            location = "/expectedSongsAfterDelete.json"
    )
    public void deleteASongById() {
        SongManagement songManager = new SongManagement(MongoDbUtil.getCollection("song"));

        // ID này phải khớp với _id trong initialSongs.json
        String idToDelete = "64b8f0d2a3c1f24e5bc12345";
        songManager.deleteById(idToDelete);
    }
}
