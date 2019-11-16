package fpt.dps.dtms.web.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import fpt.dps.dtms.service.CommunicationService;

@Component
public class ContextRefreshedHandler implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(ContextRefreshedHandler.class);

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			// Initialize the template for web socket messages
			CommunicationService.setTemplate(messagingTemplate);
		} catch (Exception ex) {
			log.error(getClass().getName(), ex);
		}
	}
	
}
