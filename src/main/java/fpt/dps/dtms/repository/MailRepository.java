package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Mail;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Mail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MailRepository extends JpaRepository<Mail, Long>, JpaSpecificationExecutor<Mail> {
	@Query("select mailReceiver.mail from MailReceiver mailReceiver where mailReceiver.to =:userLogin order by mailReceiver.createdDate desc")
	Page<Mail> getAllMailByUserLogin(@Param("userLogin") String userLogin, Pageable pageable);
	
	@Query("select mailReceiver.mail from MailReceiver mailReceiver where mailReceiver.to =:userLogin and mailReceiver.status = false order by mailReceiver.createdDate desc")
	Page<Mail> getAllMailUnreadByUserLogin(@Param("userLogin") String userLogin, Pageable pageable);
	
	@Query("select count(mailReceiver.mail) from MailReceiver mailReceiver where mailReceiver.to =:userLogin")
	Integer countAllMailInboxByUserLogin(@Param("userLogin") String userLogin);
	
	@Query("select count(mailReceiver.mail) from MailReceiver mailReceiver where mailReceiver.to =:userLogin and mailReceiver.status = false")
	Integer countAllMailUnseenByUserLogin(@Param("userLogin") String userLogin);
	
	@Query("select mailReceiver.mail from MailReceiver mailReceiver where mailReceiver.from LIKE CONCAT('%',:query,'%')")
	Page<Mail> searchMailByTitleAndSender(@Param("query") String query, Pageable pageable);
	
	@Query("select mailReceiver.mail from MailReceiver mailReceiver where mailReceiver.from =:userLogin order by mailReceiver.createdDate desc")
	Page<Mail> getAllMailSendByUserLogin(@Param("userLogin") String userLogin, Pageable pageable);
}
