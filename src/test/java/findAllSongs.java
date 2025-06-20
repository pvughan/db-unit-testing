import javax.inject.Inject;
import com.mongodb.client.MongoClient;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@UsingDataSet(locations = "/dataset/initialSongs.json",
        loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
public class findAllSongs {
    @ClassRule
    public static ManagedMongoDb embeddedMongo = new ManagedMongoDbRule().build();

    @Rule
    public MongoDbRule remoteMongo = new MongoDbRule().defaultManagedMongoDb("test");

    @Inject
    private MongoClient mongo;   // <- đây là field được NoSQL-Unit tự động inject

    @Test
    @UsingDataSet(locations = "initialSongs.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "expectedSongs.json")
    public void findAllSongs_shouldReturnAllLoaded() {
        SongManagement manager = new SongManagement(
                mongo.getDatabase("test")
                        .getCollection("songs")
        );
        var songs = manager.findAll();
        assertThat(songs, hasSize(2));
        assertThat(new Song("Bohemian Rhapsody","Queen"), isIn(songs));
        assertThat(new Song("Imagine","John Lennon"),  isIn(songs));
    }
}
