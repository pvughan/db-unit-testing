import ...
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
