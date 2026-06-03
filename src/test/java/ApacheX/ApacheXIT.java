package ApacheX;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApacheXIT extends AbstractIntegrationTest {
  @Test
  void contextTest() {
    assertTrue(Initializer.kafka.isRunning());
  }
}
