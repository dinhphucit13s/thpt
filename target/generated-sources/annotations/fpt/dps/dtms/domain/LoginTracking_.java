package fpt.dps.dtms.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(LoginTracking.class)
public abstract class LoginTracking_ {

	public static volatile SingularAttribute<LoginTracking, Instant> startTime;
	public static volatile SingularAttribute<LoginTracking, Long> id;
	public static volatile SingularAttribute<LoginTracking, Instant> endTime;
	public static volatile SingularAttribute<LoginTracking, String> login;

}

