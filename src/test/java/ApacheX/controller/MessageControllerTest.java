package ApacheX.controller;

import ApacheX.model.MessageVO;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageControllerTest {
  @Test
  void latestMessagesKeepsOnlyRequestedNewestMessages() {
    final var first = messageAt(1);
    final var second = messageAt(2);
    final var third = messageAt(3);

    assertEquals(List.of(second, third), MessageController.latestMessages(List.of(first, second, third), 2));
  }

  private static MessageVO messageAt(long timestamp) {
    final var message = new MessageVO();
    message.setTimestamp(new Date(timestamp));
    return message;
  }
}
