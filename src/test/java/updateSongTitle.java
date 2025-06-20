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

public class updateSongTitle {
    @ClassRule
    public static ManagedMongoDbRule embeddedMongo = new ManagedMongoDbRule().build();

    @Rule
    public MongoDbRule mongo = newMongoDbRule().defaultManagedMongoDb("test").build();

    @Test
    public void whenUpdateTitle_thenTitleIsChanged() {
        var collection = MongoDbUtil.getCollection("songs");
        SongManagement mgr = new SongManagement(collection);

        // đổi "Song A" -> "Song A Updated"
        mgr.updateTitle("Song A", "Song A Updated");

        List<Song> found = mgr.findByTitle("Song A Updated");
        assertEquals(1, found.size());
        assertEquals("Song A Updated", found.get(0).getTitle());
    }
}

