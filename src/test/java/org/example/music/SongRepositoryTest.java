package org.example.music;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SongRepositoryTest {

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test");

    @Autowired
    private SongRepository songRepository;

    @Test
    @UsingDataSet(locations = "songs.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void findById_shouldReturnSongIfExists() {
        Song found = songRepository.findById("1").orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Bohemian Rhapsody");
    }

    // Các test khác tương tự...
}