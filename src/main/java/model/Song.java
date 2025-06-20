package model;

public class Song {

    private String id;
    private String title;
    private String artist;
    // **Constructor không tham số** để SongRepository có thể dùng new Song()
    public Song() {
    }

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    // getter / setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
}