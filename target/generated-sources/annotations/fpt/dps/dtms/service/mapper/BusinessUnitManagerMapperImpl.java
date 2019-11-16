package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.BusinessUnit;
import fpt.dps.dtms.domain.BusinessUnitManager;
import fpt.dps.dtms.domain.User;
import fpt.dps.dtms.service.dto.BusinessUnitManagerDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:11:59+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class BusinessUnitManagerMapperImpl implements BusinessUnitManagerMapper {

    @Autowired
    private BusinessUnitMapper businessUnitMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<BusinessUnitManager> toEntity(List<BusinessUnitManagerDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<BusinessUnitManager> list = new ArrayList<BusinessUnitManager>( dtoList.size() );
        for ( BusinessUnitManagerDTO businessUnitManagerDTO : dtoList ) {
            list.add( toEntity( businessUnitManagerDTO ) );
        }

        return list;
    }

    @Override
    public List<BusinessUnitManagerDTO> toDto(List<BusinessUnitManager> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<BusinessUnitManagerDTO> list = new ArrayList<BusinessUnitManagerDTO>( entityList.size() );
        for ( BusinessUnitManager businessUnitManager : entityList ) {
            list.add( toDto( businessUnitManager ) );
        }

        return list;
    }

    @Override
    public BusinessUnitManagerDTO toDto(BusinessUnitManager businessUnitManager) {
        if ( businessUnitManager == null ) {
            return null;
        }

        BusinessUnitManagerDTO businessUnitManagerDTO = new BusinessUnitManagerDTO();

        String name = businessUnitManagerBusinessUnitName( businessUnitManager );
        if ( name != null ) {
            businessUnitManagerDTO.setBusinessUnitName( name );
        }
        Long id = businessUnitManagerManagerId( businessUnitManager );
        if ( id != null ) {
            businessUnitManagerDTO.setManagerId( id );
        }
        String login = businessUnitManagerManagerLogin( businessUnitManager );
        if ( login != null ) {
            businessUnitManagerDTO.setManagerLogin( login );
        }
        Long id1 = businessUnitManagerBusinessUnitId( businessUnitManager );
        if ( id1 != null ) {
            businessUnitManagerDTO.setBusinessUnitId( id1 );
        }
        businessUnitManagerDTO.setCreatedBy( businessUnitManager.getCreatedBy() );
        businessUnitManagerDTO.setCreatedDate( businessUnitManager.getCreatedDate() );
        businessUnitManagerDTO.setLastModifiedBy( businessUnitManager.getLastModifiedBy() );
        businessUnitManagerDTO.setLastModifiedDate( businessUnitManager.getLastModifiedDate() );
        businessUnitManagerDTO.setId( businessUnitManager.getId() );
        businessUnitManagerDTO.setDescription( businessUnitManager.getDescription() );
        businessUnitManagerDTO.setStartTime( businessUnitManager.getStartTime() );
        businessUnitManagerDTO.setEndTime( businessUnitManager.getEndTime() );

        return businessUnitManagerDTO;
    }

    @Override
    public BusinessUnitManager toEntity(BusinessUnitManagerDTO businessUnitManagerDTO) {
        if ( businessUnitManagerDTO == null ) {
            return null;
        }

        BusinessUnitManager businessUnitManager = new BusinessUnitManager();

        businessUnitManager.setManager( userMapper.userFromId( businessUnitManagerDTO.getManagerId() ) );
        businessUnitManager.setBusinessUnit( businessUnitMapper.fromId( businessUnitManagerDTO.getBusinessUnitId() ) );
        businessUnitManager.setCreatedBy( businessUnitManagerDTO.getCreatedBy() );
        businessUnitManager.setCreatedDate( businessUnitManagerDTO.getCreatedDate() );
        businessUnitManager.setLastModifiedBy( businessUnitManagerDTO.getLastModifiedBy() );
        businessUnitManager.setLastModifiedDate( businessUnitManagerDTO.getLastModifiedDate() );
        businessUnitManager.setId( businessUnitManagerDTO.getId() );
        businessUnitManager.setDescription( businessUnitManagerDTO.getDescription() );
        businessUnitManager.setStartTime( businessUnitManagerDTO.getStartTime() );
        businessUnitManager.setEndTime( businessUnitManagerDTO.getEndTime() );

        return businessUnitManager;
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
