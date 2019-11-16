package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.LoginTracking;
import fpt.dps.dtms.service.dto.LoginTrackingDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:11:59+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class LoginTrackingMapperImpl implements LoginTrackingMapper {

    @Override
    public LoginTracking toEntity(LoginTrackingDTO dto) {
        if ( dto == null ) {
            return null;
        }

        LoginTracking loginTracking = new LoginTracking();

        loginTracking.setId( dto.getId() );
        loginTracking.setLogin( dto.getLogin() );
        loginTracking.setStartTime( dto.getStartTime() );
        loginTracking.setEndTime( dto.getEndTime() );

        return loginTracking;
    }

    @Override
    public LoginTrackingDTO toDto(LoginTracking entity) {
        if ( entity == null ) {
            return null;
        }

        LoginTrackingDTO loginTrackingDTO = new LoginTrackingDTO();

        loginTrackingDTO.setId( entity.getId() );
        loginTrackingDTO.setLogin( entity.getLogin() );
        loginTrackingDTO.setStartTime( entity.getStartTime() );
        loginTrackingDTO.setEndTime( entity.getEndTime() );

        return loginTrackingDTO;
    }

    @Override
    public List<LoginTracking> toEntity(List<LoginTrackingDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<LoginTracking> list = new ArrayList<LoginTracking>( dtoList.size() );
        for ( LoginTrackingDTO loginTrackingDTO : dtoList ) {
            list.add( toEntity( loginTrackingDTO ) );
        }

        return list;
    }

    @Override
    public List<LoginTrackingDTO> toDto(List<LoginTracking> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<LoginTrackingDTO> list = new ArrayList<LoginTrackingDTO>( entityList.size() );
        for ( LoginTracking loginTracking : entityList ) {
            list.add( toDto( loginTracking ) );
        }

        return list;
    }
}
