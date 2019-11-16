package fpt.dps.dtms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fpt.dps.dtms.domain.Issues;
import fpt.dps.dtms.domain.Notification;
import fpt.dps.dtms.repository.NotificationRepository;
import fpt.dps.dtms.repository.search.NotificationSearchRepository;
import fpt.dps.dtms.service.dto.NotificationDTO;
import fpt.dps.dtms.service.mapper.NotificationMapper;
import io.github.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class NotificationQueryService extends QueryService<Issues> {

	private final Logger log = LoggerFactory.getLogger(IssuesQueryService.class);
	
	private final NotificationRepository notificationRepository;
	
	private final NotificationSearchRepository notificationSearchRepository;
	
	private final NotificationMapper notificationMapper;

	public NotificationQueryService(NotificationRepository notificationRepository,
			NotificationSearchRepository notificationSearchRepository, NotificationMapper notificationMapper) {
		this.notificationRepository = notificationRepository;
		this.notificationSearchRepository = notificationSearchRepository;
		this.notificationMapper = notificationMapper;
	}

	public Page<NotificationDTO> getNotificationList(String userName, String modeViewNotify, Pageable pageable) {
		return this.notificationRepository.findByUserName(userName, modeViewNotify, pageable).map(notificationMapper::toDto);
	}

	public Integer countNotificationUnread(String userLogin) {
		// TODO Auto-generated method stub
		return this.notificationRepository.countNotificationUnread(userLogin);
	}
	
	
}
