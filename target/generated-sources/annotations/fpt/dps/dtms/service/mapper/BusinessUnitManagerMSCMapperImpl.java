package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.domain.BusinessUnitManager;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.service.dto.BusinessUnitManagerMSCDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T23:36:19+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class BusinessUnitManagerMSCMapperImpl implements BusinessUnitManagerMSCMapper {

    @Override
    public List<BusinessUnitManagerMSCDTO> toDto(List<BusinessUnitManager> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<BusinessUnitManagerMSCDTO> list = new ArrayList<BusinessUnitManagerMSCDTO>( arg0.size() );
        for ( BusinessUnitManager businessUnitManager : arg0 ) {
            list.add( toDto( businessUnitManager ) );
        }

        return list;
    }

    @Override
    public BusinessUnitManager toEntity(BusinessUnitManagerMSCDTO arg0) {
        if ( arg0 == null ) {
            return null;
        }

        BusinessUnitManager businessUnitManager = new BusinessUnitManager();

        businessUnitManager.setId( arg0.getId() );

        return businessUnitManager;
    }

    @Override
    public List<BusinessUnitManager> toEntity(List<BusinessUnitManagerMSCDTO> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<BusinessUnitManager> list = new ArrayList<BusinessUnitManager>( arg0.size() );
        for ( BusinessUnitManagerMSCDTO businessUnitManagerMSCDTO : arg0 ) {
            list.add( toEntity( businessUnitManagerMSCDTO ) );
        }

        return list;
    }

    @Override
    public BusinessUnitManagerMSCDTO toDto(BusinessUnitManager businessUnitManager) {
        if ( businessUnitManager == null ) {
            return null;
        }

        BusinessUnitManagerMSCDTO businessUnitManagerMSCDTO = new BusinessUnitManagerMSCDTO();

        String name = businessUnitManagerBusinessUnitName( businessUnitManager );
        if ( name != null ) {
            businessUnitManagerMSCDTO.setBusinessUnitName( name );
        }
        Long id = businessUnitManagerManagerId( businessUnitManager );
        if ( id != null ) {
            businessUnitManagerMSCDTO.setManagerId( id );
        }
        String login = businessUnitManagerManagerLogin( businessUnitManager );
        if ( login != null ) {
            businessUnitManagerMSCDTO.setManagerLogin( login );
        }
        Long id1 = businessUnitManagerBusinessUnitId( businessUnitManager );
        if ( id1 != null ) {
            businessUnitManagerMSCDTO.setBusinessUnitId( id1 );
        }
        businessUnitManagerMSCDTO.setId( businessUnitManager.getId() );

        return businessUnitManagerMSCDTO;
    }

    private String businessUnitManagerBusinessUnitName(BusinessUnitManager businessUnitManager) {
        if ( businessUnitManager == null ) {
            return null;
        }
        BusinessUnit businessUnit = businessUnitManager.getBusinessUnit();
        if ( businessUnit == null ) {
            return null;
        }
        String name = businessUnit.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long businessUnitManagerManagerId(BusinessUnitManager businessUnitManager) {
        if ( businessUnitManager == null ) {
            return null;
        }
        User manager = businessUnitManager.getManager();
        if ( manager == null ) {
            return null;
        }
        Long id = manager.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String businessUnitManagerManagerLogin(BusinessUnitManager businessUnitManager) {
        if ( businessUnitManager == null ) {
            return null;
        }
        User manager = businessUnitManager.getManager();
        if ( manager == null ) {
            return null;
        }
        String login = manager.getLogin();
        if ( login == null ) {
            return null;
        }
        return login;
    }

    private Long businessUnitManagerBusinessUnitId(BusinessUnitManager businessUnitManager) {
        if ( businessUnitManager == null ) {
            return null;
        }
        BusinessUnit businessUnit = businessUnitManager.getBusinessUnit();
        if ( businessUnit == null ) {
            return null;
        }
        Long id = businessUnit.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
