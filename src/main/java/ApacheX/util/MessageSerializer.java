package ApacheX.util;

@FunctionalInterface
public interface MessageSerializer {
  byte[] serializeMessage(String value);
}
