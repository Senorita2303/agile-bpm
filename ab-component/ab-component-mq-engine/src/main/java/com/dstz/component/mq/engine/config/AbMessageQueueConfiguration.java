package com.dstz.component.mq.engine.config;

import com.dstz.component.mq.engine.consumer.CommonMessageQueueConsumer;
import com.dstz.component.mq.api.constants.JmsDestinationConstant;
import com.dstz.component.mq.api.producer.JmsProducer;
import com.dstz.component.mq.engine.producer.CommonMessageQueueProducer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.ConnectionFactory;


/**
 * Generic message queue autowiring
 *
 */
@Conditional(AbMessageQueueConditional.class)
@EnableJms
public class AbMessageQueueConfiguration {

    @Bean
    public JmsProducer jmsProducer() {

        return new CommonMessageQueueProducer();
    }

    @Bean
    public CommonMessageQueueConsumer messageQueueConsumer() {

        return new CommonMessageQueueConsumer();
    }

    @Bean
    public MessageListenerAdapter commonMessageQueueConsumerListenerAdapter(CommonMessageQueueConsumer messageQueueConsumer) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter();
        messageListenerAdapter.setDelegate(messageQueueConsumer);
        return messageListenerAdapter;
    }

    @Bean
    public DefaultMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                                    @Qualifier("commonMessageQueueConsumerListenerAdapter") MessageListenerAdapter messageListenerAdapter) {
        DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        messageListenerContainer.setDestinationName(JmsDestinationConstant.DEFAULT_NAME);
        messageListenerContainer.setMessageListener(messageListenerAdapter);
        messageListenerContainer.setConnectionFactory(connectionFactory);
        return messageListenerContainer;
    }


}
