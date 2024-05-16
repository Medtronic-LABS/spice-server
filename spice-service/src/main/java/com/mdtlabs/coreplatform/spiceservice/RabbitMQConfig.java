package com.mdtlabs.coreplatform.spiceservice;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up RabbitMQ configurations.
 * This class defines beans for queue, exchange, and binding,
 * as well as message converter and AMQP template.
 * <p>
 * Author: Hemavardhini Jothi
 * created on February 22, 2024
 */
@Configuration
public class RabbitMQConfig    {

        @Value("${rabbitmq.queue.name}")
        private String queueName;

        @Value("${rabbitmq.exchange.name}")
        private String exchangeName;

        @Value("${rabbitmq.routing.key.name}")
        private String routingKey;

        /**
         * Creates a bean for the message queue.
         *
         * @return Queue object with the configured name.
         */
        @Bean
        public Queue queue(){
                return new Queue(queueName);
        }

        /**
         * Creates a bean for the topic exchange.
         *
         * @return TopicExchange object with the configured name.
         */
        @Bean
        public TopicExchange exchange(){
                return new TopicExchange(exchangeName);
        }

        /**
         * Creates a bean for the binding between queue and exchange.
         *
         * @return Binding object with the configured queue, exchange, and routing key.
         */
        @Bean
        public Binding binding(){
               return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
        }


        /**
         * Creates a bean for the message converter.
         *
         * @return Jackson2JsonMessageConverter object.
         */
        @Bean
        public MessageConverter messageConverter() {
                return new Jackson2JsonMessageConverter();
        }

        /**
         * Creates a bean for the AMQP template.
         *
         * @param connectionFactory The connection factory.
         * @return RabbitTemplate object configured with message converter.
         */
        @Bean
        public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
                RabbitTemplate template = new RabbitTemplate(connectionFactory);
                template.setMessageConverter(messageConverter());
                return template;
        }
}
