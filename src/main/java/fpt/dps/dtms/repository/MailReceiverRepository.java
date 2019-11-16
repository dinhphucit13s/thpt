package fpt.dps.dtms.repository;

import fpt.dps.dtms.domain.Mail;
import fpt.dps.dtms.domain.MailReceiver;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the MailReceiver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MailReceiverRepository extends JpaRepository<MailReceiver, Long>, JpaSpecificationExecutor<MailReceiver> {
	@Query("select mailReceiver from MailReceiver mailReceiver where mailReceiver.to =:userLogin and mailReceiver.mail.id =:id")
	MailReceiver getMailToByMailId(@Param("userLogin") String userLogin, @Param("id") Long id);
	
	@Query("select mailReceiver from MailReceiver mailReceiver where mailReceiver.from =:userLogin and mailReceiver.mail.id =:id")
	MailReceiver getMailFromByMailId(@Param("userLogin") String userLogin, @Param("id") Long id);
}
