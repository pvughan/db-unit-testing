package org.example.music;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class SongRepository {

    private final MongoCollection<Document> songs;

    public SongRepository(MongoCollection<Document> songs) {
        this.songs = songs;
    }

    /** Lấy hết tất cả các bản ghi Song */
    public List<Song> findAll() {
        List<Song> result = new ArrayList<>();
        songs.find().forEach((Consumer<Document>) doc -> {
            result.add(documentToSong(doc));
        });
        return result;
    }

    /** Lưu một Song mới vào collection, trả về Song kèm id được tạo */
    public Song save(Song song) {
        Document doc = new Document()
                .append("title", song.getTitle())
                .append("artist", song.getArtist());
        songs.insertOne(doc);
        String id = doc.getObjectId("_id").toHexString();
        song.setId(id);
        return song;
    }

    /** Tìm một Song theo id, trả về null nếu không tìm thấy */
    public Song findById(String id) {
        Document doc = songs.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToSong(doc) : null;
    }

    /** Tìm tất cả Song có title khớp (exact match) */
    public List<Song> findByTitle(String title) {
        List<Song> result = new ArrayList<>();
        songs.find(Filters.eq("title", title)).forEach((Consumer<Document>) doc -> {
            result.add(documentToSong(doc));
        });
        return result;
    }

    /** Xóa một Song theo id */
    public void deleteById(String id) {
        songs.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

    /** Chuyển đổi BSON Document thành đối tượng Song */
    private Song documentToSong(Document doc) {
        Song s = new Song();
        s.setId(doc.getObjectId("_id").toHexString());
        s.setTitle(doc.getString("title"));
        s.setArtist(doc.getString("artist"));
        return s;
    }
}