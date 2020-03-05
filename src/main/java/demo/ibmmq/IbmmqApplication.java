package demo.ibmmq;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@SpringBootApplication
@EnableJms
@RestController
public class IbmmqApplication {

	@Autowired
	private JmsTemplate jmsTemplate;

	public static void main(String[] args) {
		SpringApplication.run(IbmmqApplication.class, args);
	}

	@Bean
	JmsTemplate jmsTemplate(ConnectionFactory connFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connFactory);

		//https://developer.ibm.com/messaging/2018/02/09/mq-spring-tip/
		jmsTemplate.setReceiveTimeout(60000L);
		jmsTemplate.setPubSubDomain(false);
		
		return jmsTemplate;
	}

	@GetMapping("/{queue:.+}")
	public String getMessage(@PathVariable(name = "queue") String queue) {
		return (String) jmsTemplate.receiveAndConvert(queue);

	}

	@PostMapping("/{queue:.+}")
	public String publishMessage(@PathVariable(name = "queue") String queue,
			@RequestParam(name="file", required=false) MultipartFile contentFile,
			@RequestParam(name="message", required=false) String message) throws Exception {
		Assert.isTrue(contentFile!=null || message != null, "Must use file or message");
		Assert.isTrue(!(contentFile!=null &&message != null), "cannot use both file and message");
		
		jmsTemplate.convertAndSend(queue, contentFile != null ? new String(contentFile.getBytes()): message);
		return "message was published to " + queue;
	}
}
