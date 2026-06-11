package ApacheX.controller;

import ApacheX.config.MessageFormatConfiguration.MessageFormatProperties;
import ApacheX.config.ProtobufDescriptorConfiguration.ProtobufDescriptorProperties;
import ApacheX.config.SchemaRegistryConfiguration.SchemaRegistryProperties;
import ApacheX.model.AclVO;
import ApacheX.model.BrokerVO;
import ApacheX.model.ClusterSummaryVO;
import ApacheX.model.ConsumerVO;
import ApacheX.model.CreateMessageVO;
import ApacheX.model.CreateTopicVO;
import ApacheX.model.MessageVO;
import ApacheX.model.SearchResultsVO;
import ApacheX.model.TopicVO;
import ApacheX.service.KafkaMonitor;
import ApacheX.service.MessageInspector;
import ApacheX.util.Deserializers;
import ApacheX.util.IntMessageDeserializer;
import ApacheX.util.MessageFormat;
import ApacheX.util.Serializers;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageControllerTest {
  @Test
  void latestMessagesKeepsOnlyRequestedNewestMessages() {
    final var first = messageAt(1);
    final var second = messageAt(2);
    final var third = messageAt(3);

    assertEquals(List.of(second, third), MessageController.latestMessages(List.of(first, second, third), 2));
  }

  @Test
  void addMessagePreservesConfiguredKeyFormat() {
    final var topicName = "topic";
    final var topicPartition = new TopicPartition(topicName, 0);
    final var recordMetadata = new RecordMetadata(topicPartition, 0, 0, 42, 0, 0);
    final var kafkaMonitor = new FakeKafkaMonitor(topicName, recordMetadata);
    final var messageInspector = new MessageInspector(kafkaMonitor);

    final var messageFormatProperties = new MessageFormatProperties();
    messageFormatProperties.setFormat(MessageFormat.MSGPACK);
    messageFormatProperties.setKeyFormat(MessageFormat.DEFAULT);

    final var controller = new MessageController(
      kafkaMonitor,
      messageInspector,
      messageFormatProperties,
      new SchemaRegistryProperties(),
      new ProtobufDescriptorProperties(),
      true);

    final var message = new CreateMessageVO();
    message.setTopicPartition(0);
    final var model = new ExtendedModelMap();

    controller.addMessage(topicName, message, model);

    final var messageForm = (MessageController.PartitionOffsetInfo) model.getAttribute("messageForm");
    assertEquals(MessageFormat.MSGPACK, messageForm.getFormat());
    assertEquals(MessageFormat.DEFAULT, messageForm.getKeyFormat());
  }

  @Test
  void getPartitionOrMessagesSupportsIntMessageFormat() {
    final var topicName = "topic";
    final var topicPartition = new TopicPartition(topicName, 0);
    final var recordMetadata = new RecordMetadata(topicPartition, 0, 0, 42, 0, 0);
    final var kafkaMonitor = new FakeKafkaMonitor(topicName, recordMetadata);
    final var messageInspector = new MessageInspector(kafkaMonitor);

    final var controller = new MessageController(
      kafkaMonitor,
      messageInspector,
      new MessageFormatProperties(),
      new SchemaRegistryProperties(),
      new ProtobufDescriptorProperties(),
      true);

    controller.getPartitionOrMessages(topicName, 0, 0L, 1, "INT", "INT", null, null, false);

    assertEquals(IntMessageDeserializer.class, kafkaMonitor.lastDeserializers.getKeyDeserializer().getClass());
    assertEquals(IntMessageDeserializer.class, kafkaMonitor.lastDeserializers.getValueDeserializer().getClass());
  }

  private static MessageVO messageAt(long timestamp) {
    final var message = new MessageVO();
    message.setTimestamp(new Date(timestamp));
    return message;
  }

  private static final class FakeKafkaMonitor implements KafkaMonitor {
    private final String topicName;
    private final RecordMetadata recordMetadata;
    private Deserializers lastDeserializers;

    private FakeKafkaMonitor(String topicName, RecordMetadata recordMetadata) {
      this.topicName = topicName;
      this.recordMetadata = recordMetadata;
    }

    @Override
    public List<BrokerVO> getBrokers() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Optional<BrokerVO> getBroker(int id) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<TopicVO> getTopics() {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<MessageVO> getMessages(String topic, int count, Deserializers deserializers) {
      return Collections.emptyList();
    }

    @Override
    public List<MessageVO> getMessages(TopicPartition topicPartition, long offset, int count,
                                       Deserializers deserializers) {
      lastDeserializers = deserializers;
      return Collections.emptyList();
    }

    @Override
    public Optional<TopicVO> getTopic(String topic) {
      return topicName.equals(topic) ? Optional.of(new TopicVO(topic)) : Optional.empty();
    }

    @Override
    public ClusterSummaryVO getClusterSummary(Collection<TopicVO> topics) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<ConsumerVO> getConsumersByGroup(String groupId) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<ConsumerVO> getConsumersByTopics(Collection<TopicVO> topicVos) {
      throw new UnsupportedOperationException();
    }

    @Override
    public SearchResultsVO searchMessages(String topic, String searchString, Integer partition, Integer maximumCount,
                                          Date startTimestamp, Deserializers deserializers) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void createTopic(CreateTopicVO createTopicDto) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void deleteTopic(String topic) {
      throw new UnsupportedOperationException();
    }

    @Override
    public RecordMetadata publishMessage(CreateMessageVO message, Serializers serializers) {
      return recordMetadata;
    }

    @Override
    public List<AclVO> getAcls() {
      throw new UnsupportedOperationException();
    }
  }
}
