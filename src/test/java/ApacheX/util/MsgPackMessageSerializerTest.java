package ApacheX.util;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MsgPackMessageSerializerTest {
  @Test
  void serializedMessagesCanBeDeserialized() {
    final var serializer = new MsgPackMessageSerializer();
    final var deserializer = new MsgPackMessageDeserializer();

    assertEquals("\"hello\"", deserializer.deserializeMessage(ByteBuffer.wrap(serializer.serializeMessage("hello"))));
  }
}
