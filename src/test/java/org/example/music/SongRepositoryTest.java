package org.example.music;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;
import org.example.music.Song;
import org.example.music.SongRepository;

public class SongRepositoryTest {

    private static MongoClient client;
    private static MongoCollection<Document> coll;
    private SongRepository repo;

    @BeforeClass
    public static void beforeClass() {
        // Kết nối in-memory hoặc thật tùy bạn (ví dụ local Mongo)
        client = MongoClients.create("mongodb://localhost:27017");
        coll = client.getDatabase("testdb").getCollection("songs");
    }

    @Before
    public void setUp() {
        coll.drop();                           // Làm sạch trước mỗi test
        repo = new SongRepository(coll);       // Truyền đúng MongoCollection vào constructor
    }

    @AfterClass
    public static void afterClass() {
        client.close();
    }

    @Test
    public void save_shouldPersistNewSong() {
        Song s = new Song(null, "Yesterday", "The Beatles");
        Song saved = repo.save(s);
        assertNotNull(saved.getId());
    }

    @Test
    public void findById_shouldReturnSongWhenExists() {
        Song s = repo.save(new Song(null, "Hey Jude", "The Beatles"));
        Song found = repo.findById(s.getId());
        assertNotNull(found);
        assertEquals("Hey Jude", found.getTitle());
    }

    @Test
    public void findByTitle_shouldReturnMatchingSongs() {
        repo.save(new Song(null, "Let It Be", "The Beatles"));
        repo.save(new Song(null, "Let It Be", "John Doe"));
        List<Song> list = repo.findByTitle("Let It Be");
        assertEquals(2, list.size());
    }

    @Test
    public void deleteById_shouldRemoveSong() {
        Song s1 = repo.save(new Song(null, "Come Together", "The Beatles"));
        Song s2 = repo.save(new Song(null, "Something", "The Beatles"));

        // Xóa s1
        repo.deleteById(s1.getId());

        // Xác nhận s1 đã mất
        assertNull(repo.findById(s1.getId()));
        // Và chỉ còn 1 bản ghi
        assertEquals(1, repo.findAll().size());
    }
}