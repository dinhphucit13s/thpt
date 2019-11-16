package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Authority;
import fpt.dps.dtms.service.dto.AuthorityDTO;
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
public class AuthorityMapperImpl implements AuthorityMapper {

    @Override
    public Authority toEntity(AuthorityDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Authority authority = new Authority();

        authority.setName( dto.getName() );

        return authority;
    }

    @Override
    public AuthorityDTO toDto(Authority entity) {
        if ( entity == null ) {
            return null;
        }

        AuthorityDTO authorityDTO = new AuthorityDTO();

        authorityDTO.setName( entity.getName() );

        return authorityDTO;
    }

    @Override
    public List<Authority> toEntity(List<AuthorityDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Authority> list = new ArrayList<Authority>( dtoList.size() );
        for ( AuthorityDTO authorityDTO : dtoList ) {
            list.add( toEntity( authorityDTO ) );
        }

        return list;
    }

    @Override
    public List<AuthorityDTO> toDto(List<Authority> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AuthorityDTO> list = new ArrayList<AuthorityDTO>( entityList.size() );
        for ( Authority authority : entityList ) {
            list.add( toDto( authority ) );
        }

        return list;
    }
}
