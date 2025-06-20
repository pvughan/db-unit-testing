import ...
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
