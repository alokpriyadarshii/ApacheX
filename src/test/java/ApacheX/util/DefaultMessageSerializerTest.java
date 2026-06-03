package ApacheX.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DefaultMessageSerializerTest {
  @Test
  void serializeMessageUsesUtf8() {
    final var serializer = new DefaultMessageSerializer();

    assertArrayEquals("Grüße".getBytes(StandardCharsets.UTF_8), serializer.serializeMessage("Grüße"));
  }
}
