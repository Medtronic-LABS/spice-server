package com.mdtlabs.coreplatform.spiceservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RabbitMQConfigTest {

    @InjectMocks
    private RabbitMQConfig rabbitMQConfig;

    @Mock
    private ConnectionFactory connectionFactory;

    @Test
    public void testQueue() {
        //given
        ReflectionTestUtils.setField(rabbitMQConfig, "queueName", "name");
        //then
        Queue queue = rabbitMQConfig.queue();
        assertNotNull(queue);
    }

    @Test
    public void testExchange() {
        //given
        ReflectionTestUtils.setField(rabbitMQConfig, "exchangeName", "name");
        //then
        TopicExchange exchange = rabbitMQConfig.exchange();
        assertNotNull(exchange);
    }

    @Test
    public void testBinding() {
        //given
        ReflectionTestUtils.setField(rabbitMQConfig, "routingKey", "routingKey");
        ReflectionTestUtils.setField(rabbitMQConfig, "queueName", "name");
        //then
        Binding binding = rabbitMQConfig.binding();
        assertNotNull(binding);
    }

    @Test
    public void testMessageConverter() {
        MessageConverter messageConverter = rabbitMQConfig.messageConverter();
        assertNotNull(messageConverter);
    }

    @Test
    public void testAmqpTemplate() {
        when(connectionFactory.createConnection()).thenReturn(null);
        assertNotNull(rabbitMQConfig.amqpTemplate(connectionFactory));
    }
}
