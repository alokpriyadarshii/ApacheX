package ApacheX.util;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteUtilsTest {
  @Test
  void readStringRespectsBufferPositionAndLimit() {
    final var buffer = ByteBuffer.wrap("xxhelloYY".getBytes(StandardCharsets.UTF_8));
    buffer.position(2);
    buffer.limit(7);

    assertEquals("hello", ByteUtils.readString(buffer));
    assertEquals(2, buffer.position());
    assertEquals(7, buffer.limit());
  }

  @Test
  void readStringRespectsDirectBufferPositionAndLimit() {
    final var buffer = ByteBuffer.allocateDirect(9);
    buffer.put("xxhelloYY".getBytes(StandardCharsets.UTF_8));
    buffer.position(2);
    buffer.limit(7);

    assertEquals("hello", ByteUtils.readString(buffer));
    assertEquals(2, buffer.position());
    assertEquals(7, buffer.limit());
  }
}
