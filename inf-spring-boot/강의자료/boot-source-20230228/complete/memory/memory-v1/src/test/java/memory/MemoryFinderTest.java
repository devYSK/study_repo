package memory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemoryFinderTest {

    @Test
    void get() {
        MemoryFinder memoryFinder = new MemoryFinder();
        Memory memory = memoryFinder.get();
        assertThat(memory).isNotNull();
    }
}
