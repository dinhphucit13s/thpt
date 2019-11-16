package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AuthorityResource.class)
public abstract class AuthorityResource_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<AuthorityResource, String> authorityName;
	public static volatile SingularAttribute<AuthorityResource, String> name;
	public static volatile SingularAttribute<AuthorityResource, Integer> permission;
	public static volatile SingularAttribute<AuthorityResource, Long> id;

}

