package eu.kielczewski.example;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import eu.kielczewski.example.receiver.Receiver;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class AplicationTwo extends SpringBootServletInitializer {

	static String mailboxDestinationOne = "mailbox-destination-one";
	
	@Bean
	Receiver receiver() {
		return new Receiver();
	}

	@Bean
	MessageListenerAdapter adapter(Receiver receiver) {
		MessageListenerAdapter messageListener = new MessageListenerAdapter(receiver);
		messageListener.setDefaultResponseQueueName(mailboxDestinationOne);
		//messageListener.setDefaultResponseTopicName("fajnyOne");
		messageListener.setDefaultListenerMethod("receiveMessageOne");
		return messageListener;
	}

	@Bean
	SimpleMessageListenerContainer container(
			MessageListenerAdapter messageListener,
			ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setMessageListener(messageListener);
		container.setConnectionFactory(connectionFactory);
		//container.setDurableSubscriptionName("fajnyOne");
		container.setDestinationName(mailboxDestinationOne);
		return container;
	}

	public static void main(final String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AplicationTwo.class, args);
	}

	@Override
	protected final SpringApplicationBuilder configure(
			final SpringApplicationBuilder application) {
		return application.sources(AplicationTwo.class);
	}

	

}
