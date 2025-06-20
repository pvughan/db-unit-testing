import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.*;

import java.util.List;

import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.annotations.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
@UsingDataSet(locations = "/dataset/initialSongs.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)

public class findSongByIndex {
    @ClassRule
    public static ManagedMongoDbRule embeddedMongo = new ManagedMongoDbRule().build();

    @Rule
    public MongoDbRule mongo = newMongoDbRule().defaultManagedMongoDb("test").build();

    @Test
    public void whenFindByIndex_thenReturnExpectedSong() {
        var collection = MongoDbUtil.getCollection("songs");
        SongManagement mgr = new SongManagement(collection);

        // index 1 trong initialSongs.json là "Song B"
        Song s = mgr.findAll().get(1);
        assertEquals("Song B", s.getTitle());
        assertEquals("Artist 2", s.getArtist());
    }
}
