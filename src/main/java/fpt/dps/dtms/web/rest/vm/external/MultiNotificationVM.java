package fpt.dps.dtms.web.rest.vm.external;

import java.util.HashSet;
import java.util.Set;

import fpt.dps.dtms.service.dto.NotificationDTO;

public class MultiNotificationVM {
	Set<NotificationDTO> notifications = new HashSet<>();

	public Set<NotificationDTO> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<NotificationDTO> notifications) {
		this.notifications = notifications;
	}
	
	
}
