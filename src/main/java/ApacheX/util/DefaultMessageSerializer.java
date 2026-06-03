package ApacheX.util;

import java.nio.charset.StandardCharsets;

public class DefaultMessageSerializer implements MessageSerializer {

  @Override
  public byte[] serializeMessage(String value) {
    return value.getBytes(StandardCharsets.UTF_8);
  }

}
