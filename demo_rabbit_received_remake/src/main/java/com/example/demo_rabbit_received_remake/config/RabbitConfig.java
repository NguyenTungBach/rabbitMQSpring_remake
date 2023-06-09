package com.example.demo_rabbit_received_remake.config;

import com.example.demo_rabbit_received_remake.consumer.ReceiverManager;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ChannelListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String queueName = "bach-queue2";
    public static final String exchangeName = "bach-exchange2";
    public static final String routingKey = "bach-routingKey2";

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("bach-queue1");
        container.setMessageListener(listenerAdapter);
        return container;
    }

    //method is registered as a message listener in the container
    @Bean
    MessageListenerAdapter listenerAdapter(ReceiverManager receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage2");
    }

    // method creates an AMQP queue
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

//    //method creates a topic exchange
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange(exchangeName);
//    }

    //method creates a topic exchange
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

//    @Bean
//    FanoutExchange exchange() {
//        return new FanoutExchange(topicExchangeName);
//    }

    //method binds these two together (queue and exchange), defining the behavior that occurs when RabbitTemplate publishes to an exchange.
    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    //    @Bean
//    Binding binding(Queue queue, FanoutExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange);
//    }
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setRequestedHeartBeat(10);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        connectionFactory.addChannelListener(new ChannelListener() {
            boolean isHeartbeatAlertSent = false;
            @Override
            public void onCreate(Channel channel, boolean transactional) {
                channel.addShutdownListener(new ShutdownListener() {
                    @Override
                    public void shutdownCompleted(ShutdownSignalException cause) {
                        if (!isHeartbeatAlertSent){
                            System.out.println("checkHeartBeat: " + cause.getMessage() );
                            rabbitTemplate.setExchange(RabbitConfig.exchangeName);
                            rabbitTemplate.setRoutingKey(RabbitConfig.routingKey);
                            rabbitTemplate.convertAndSend(RabbitConfig.exchangeName,RabbitConfig.routingKey,"queue2 báo ngưng hoạt động");
                            isHeartbeatAlertSent = true;
                        }
                    }
                });
            }
        });
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }
}
