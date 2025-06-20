import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDb;
import com.lordofthejars.nosqlunit.mongodb.ManagedMongoDbRuleBuilder;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import org.example.music.model.SongRepository;
import org.example.music.model.Song;
import org.example.music.util.MongoDbUtil;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class addASong {
    // nếu muốn chạy một MongoDB in‐memory hoặc managed mongod cục bộ:
    @ClassRule
    public static ManagedMongoDb managedMongod = ManagedMongoDbRuleBuilder
            .newManagedMongoDbRule()
            .build();

    // rule mặc định sẽ xóa sạch db "testdb" trước mỗi @Test
    @Rule
    public MongoDbRule remoteMongoDbRule = MongoDbRule
            .mongoDbRule()
            .defaultManagedMongoDb("testdb");

    @Test
    @UsingDataSet(
            locations = { "dataset/initialSongs.json" },
            loadStrategy = LoadStrategyEnum.CLEAN_INSERT
    )
    @ShouldMatchDataSet(
            location = "dataset/songsAfterInsert.json"
    )
    public void createANewSong() {
        // 1) repo sẽ dùng collection "songs"
        SongRepository repo = new SongRepository(MongoDbUtil.getCollection("songs"));
        // 2) tạo 2 bản ghi mới
        Song s1 = new Song("Let It Be", "The Beatles");
        Song s2 = new Song("Billie Jean", "Michael Jackson");
        repo.save(s1);
        repo.save(s2);
    }
}
