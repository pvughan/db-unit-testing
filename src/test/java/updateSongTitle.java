import ...
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

