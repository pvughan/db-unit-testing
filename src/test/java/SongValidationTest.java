import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import model.Song;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public class SongValidationTest {

    private Validator validator;

    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            System.out.println("✅ " + description.getMethodName() + " passed.");
        }

        @Override
        protected void failed(Throwable e, Description description) {
            System.out.println("❌ " + description.getMethodName() + " failed: " + e.getMessage());
        }
    };

    @Before
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void testValidSong() {
        Song song = new Song("Bohemian Rhapsody", "Queen", 1975);
        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertTrue("Expected no violations", violations.isEmpty());
    }

    @Test
    public void testBlankTitle() {
        Song song = new Song(" ", "Queen", 1975);
        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertTrue("Should have violation on 'title'",
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    public void testBlankArtist() {
        Song song = new Song("Imagine", "  ", 1971);
        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertTrue("Should have violation on 'artist'",
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("artist")));
    }

    @Test
    public void testYearTooEarly() {
        Song song = new Song("Imagine", "John Lennon", 1800);
        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertTrue("Should have violation on 'year'",
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("year")));
    }

    @Test
    public void testYearTooLate() {
        Song song = new Song("Imagine", "John Lennon", 3000);
        Set<ConstraintViolation<Song>> violations = validator.validate(song);
        assertTrue("Should have violation on 'year'",
                violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("year")));
    }
}
