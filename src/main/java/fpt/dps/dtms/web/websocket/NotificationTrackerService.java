package fpt.dps.dtms.web.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class NotificationTrackerService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(NotificationTrackerService.class);

    private final SimpMessageSendingOperations messagingTemplate;

    public NotificationTrackerService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    /**
     * Notify content to user
     * @param body
     * @return notify content
     */
    @SubscribeMapping("/notification/private/{receiver}") //(1)The api which client sent to .
    @SendTo("/notification/private/{receiver}") //(2)The topic which client subscribe to. 
    //Any client subscribe to (2) will receive the message which client send to (1) 
    public String notifyToUser(@Payload String body) {
        log.debug("Notify to receiver with content {}", body);
        return body;
    }
    
    /**
     * Notify content to user
     * @param body
     * @return notify content
     */
    @SubscribeMapping("/mail/private/{receiver}") //(1)The api which client sent to .
    @SendTo("/mail/private/{receiver}") //(2)The topic which client subscribe to. 
    //Any client subscribe to (2) will receive the message which client send to (1) 
    public String mailToReceiver(@Payload String body) {
        log.debug("mail to receiver with content {}", body);
        return body;
    }
    
    /**
     * Notify content to user
     * @param body
     * @return notify content
     */
    @SubscribeMapping("/progress-import/private/{receiver}") //(1)The api which client sent to .
    @SendTo("/progress-import/private/{receiver}") //(2)The topic which client subscribe to. 
    //Any client subscribe to (2) will receive the message which client send to (1) 
    public String progressImportTaskToUser(@Payload String body) {
        log.debug("progress-import to receiver with content {}", body);
        return body;
    }
    
    /**
     * Notify content to user
     * @param body
     * @return notify content
     */
    @SubscribeMapping("/holding/private")
    @SendTo("/holding/private")
    public String sendReloadHoldingTask(@Payload String body) {
        log.debug("reload task bidding holding with content {}", body);
        return body;
    }
    
    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
    }
}
