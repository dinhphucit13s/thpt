package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserProfile.class)
public abstract class UserProfile_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<UserProfile, String> workingLocation;
	public static volatile SingularAttribute<UserProfile, String> jobTitle;
	public static volatile SingularAttribute<UserProfile, String> timezoneId;
	public static volatile SingularAttribute<UserProfile, Long> id;
	public static volatile SingularAttribute<UserProfile, User> user;

}

