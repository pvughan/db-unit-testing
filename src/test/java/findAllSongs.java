import ...
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
