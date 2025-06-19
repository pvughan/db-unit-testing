package org.example.music;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends MongoRepository<Song, String> {

    List<Song> findByTitleContaining(String title);

    List<Song> findByArtist(String artist);

    List<Song> findByReleaseYearBetween(int startYear, int endYear);
}