package fpt.dps.dtms.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import fpt.dps.dtms.service.dto.TaskBiddingDTO;

@Service
public class CommunicationService {
	
	private static final Logger log = LoggerFactory.getLogger(CommunicationService.class);
	
	private static SimpMessageSendingOperations messagingTemplate;
	
	public CommunicationService() {
	}
	
	public static void setTemplate(SimpMessageSendingOperations template) {
		messagingTemplate = template;
	}
	
	/**
	 * Notify content to a user when changing task or send email
	 * @param receiver
	 * @param body
	 */
	public static void notifyToUser(String receiver, String body) {
		String destination = "/notification/private/"+ receiver.toLowerCase();
		messagingTemplate.convertAndSend(destination, body);
	}
	
	/**
	 * mail content to a or many user when sender send email
	 * @param receiver
	 * @param body
	 */
	public static void mailToReceiver(String receiver, String body) {
		String destination = "/mail/private/"+ receiver.toLowerCase();
		messagingTemplate.convertAndSend(destination, body);
	}
	
	/**
	 * Sending progress import tasks
	 * @param receiver
	 * @param body
	 */
	public static void progressImportTaskToUser(String receiver, String body) {
		String destination = "/progress-import/private/"+ receiver.toLowerCase();
		messagingTemplate.convertAndSend(destination, body);
	}
	
	/**
	 * 
	 */
	public static void sendReloadHoldingTask(TaskBiddingDTO biddingDTO) {
		messagingTemplate.convertAndSend("/holding/private", biddingDTO);
	}
}
