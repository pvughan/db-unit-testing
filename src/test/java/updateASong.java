import ...
public class updateASong {
    @ClassRule
    public static ManagedMongoDb managedMongoDb = newInMemoryMongoDbRule().build();

    @Rule
    public MongoDbRule mongoDbRule = remoteManagedMongoDb().defaultManagedMongoDb("test");

    private MongoCollection<Document> songCollection() {
        return managedMongoDb.getClient()
                .getDatabase("test")
                .getCollection("songs");
    }

    @Test
    @UsingDataSet(
            locations = "dataset/initialSongs.json",
            loadStrategy = LoadStrategyEnum.CLEAN_INSERT
    )
    @ShouldMatchDataSet(location = "dataset/expectedSongsAfterUpdate.json")
    public void updateASong_shouldModifyTitleAndArtist() {
        // GIVEN initialSongs.json đã load

        SongManagement manager = new SongManagement(songCollection());

        // WHEN cập nhật song có id = "id-123"
        manager.updateASong("id-123", "Updated Title", "Updated Artist");

        // THEN NoSQLUnit tự động so sánh với expectedSongsAfterUpdate.json
    }
}
