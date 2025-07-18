package mongodb;

import model.Song;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Quản lý CRUD cho collection "songs" trên MongoDB.
 */
public class SongManagement {

    private final MongoCollection<Document> songs;

    /**
     * Constructor mặc định sẽ lấy collection "songs" từ MongoDbUtil.
     */
    public SongManagement(MongoCollection<Document> collection) {
        this.songs = collection;
    }

    /** Lấy tất cả Song trong collection */
    public List<Song> findAll() {
        List<Song> result = new ArrayList<>();
        songs.find()
                .forEach((Consumer<Document>) doc -> result.add(documentToSong(doc)));
        return result;
    }

    /** Lưu mới một Song, trả về Song kèm id */
    public Song save(Song song) {
        Document doc = new Document()
                .append("title", song.getTitle())
                .append("artist", song.getArtist())
                .append("year", song.getYear());
        songs.insertOne(doc);
        song.setId(doc.getObjectId("_id").toHexString());
        return song;
    }

    /** Tìm một Song theo id, trả về null nếu không tìm thấy */
    public Song findById(String id) {
        Document doc = songs.find(Filters.eq("_id", new ObjectId(id))).first();
        return (doc != null) ? documentToSong(doc) : null;
    }

    /** Tìm các Song có title khớp chính xác */
    public List<Song> findByTitle(String title) {
        List<Song> result = new ArrayList<>();
        songs.find(Filters.eq("title", title))
                .forEach((Consumer<Document>) doc -> result.add(documentToSong(doc)));
        return result;
    }

    /** Tìm các Song có artist khớp chính xác */
    public List<Song> findByArtist(String artist) {
        List<Song> result = new ArrayList<>();
        songs.find(Filters.eq("artist", artist))
                .forEach((Consumer<Document>) doc -> result.add(documentToSong(doc)));
        return result;
    }

    /** Tìm các Song có year khớp chính xác */
    public List<Song> findByYear(int year) {
        List<Song> result = new ArrayList<>();
        songs.find(Filters.eq("year", year))
                .forEach((Consumer<Document>) doc -> result.add(documentToSong(doc)));
        return result;
    }

    /** Cập nhật title, artist và year của Song theo id */
    public void updateASong(String id, String newTitle, String newArtist, int newYear) {
        songs.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.combine(
                        Updates.set("title", newTitle),
                        Updates.set("artist", newArtist),
                        Updates.set("year", newYear)
                )
        );
    }

    /** Xóa một Song theo id */
    public void deleteById(String id) {
        songs.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    /** Chuyển một Document thành đối tượng Song */
    private Song documentToSong(Document doc) {
        Song song = new Song();
        song.setId(doc.getObjectId("_id").toHexString());
        song.setTitle(doc.getString("title"));
        song.setArtist(doc.getString("artist"));
        return song;
    }
}