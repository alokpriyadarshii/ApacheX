package ApacheX.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageVOTest {
  @Test
  void getHeadersFormattedHandlesMissingHeaders() {
    final var message = new MessageVO();

    assertEquals("empty", message.getHeadersFormatted());
  }

  @Test
  void getHeadersFormattedHandlesEmptyHeaders() {
    final var message = new MessageVO();
    message.setHeaders(Map.of());

    assertEquals("empty", message.getHeadersFormatted());
  }
}
