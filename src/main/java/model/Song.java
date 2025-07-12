package model;

import jakarta.validation.constraints.*;

public class Song {

    private String id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @NotBlank(message = "Artist is required")
    @Size(max = 100, message = "Artist name must be less than 100 characters")
    private String artist;

    @Min(value = 1900, message = "Year must be after 1900")
    @Max(value = 2025, message = "Year must be before 2025")
    private int year;
    // **Constructor không tham số** để SongRepository có thể dùng new Song()
    public Song() {
    }

    public Song(String title, String artist, int year) {
        this.title = title;
        this.artist = artist;
        this.year = year;
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
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}