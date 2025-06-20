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

public class deleteASong {
    @ClassRule
    public static ManagedMongoDbRule embeddedMongo = new ManagedMongoDbRule().build();

    @Rule
    public MongoDbRule mongo = newMongoDbRule().defaultManagedMongoDb("test").build();

    @Test
    public void whenDeleteById_thenSongNotFound() {
        var collection = MongoDbUtil.getCollection("songs");
        SongManagement mgr = new SongManagement(collection);

        List<Song> before = mgr.findAll();
        assertFalse(before.isEmpty());

        String idToDelete = before.get(0).getId();
        mgr.deleteById(idToDelete);

        assertNull(mgr.findById(idToDelete));
        assertEquals(before.size() - 1, mgr.findAll().size());
    }
}
