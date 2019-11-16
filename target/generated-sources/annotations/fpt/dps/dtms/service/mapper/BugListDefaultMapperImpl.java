package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.BugListDefault;
import fpt.dps.dtms.domain.BusinessLine;
import fpt.dps.dtms.service.dto.BugListDefaultDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:00+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class BugListDefaultMapperImpl implements BugListDefaultMapper {

    @Autowired
    private BusinessLineMapper businessLineMapper;

    @Override
    public List<BugListDefault> toEntity(List<BugListDefaultDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<BugListDefault> list = new ArrayList<BugListDefault>( dtoList.size() );
        for ( BugListDefaultDTO bugListDefaultDTO : dtoList ) {
            list.add( toEntity( bugListDefaultDTO ) );
        }

        return list;
    }

    @Override
    public List<BugListDefaultDTO> toDto(List<BugListDefault> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<BugListDefaultDTO> list = new ArrayList<BugListDefaultDTO>( entityList.size() );
        for ( BugListDefault bugListDefault : entityList ) {
            list.add( toDto( bugListDefault ) );
        }

        return list;
    }

    @Override
    public BugListDefaultDTO toDto(BugListDefault bugListDefault) {
        if ( bugListDefault == null ) {
            return null;
        }

        BugListDefaultDTO bugListDefaultDTO = new BugListDefaultDTO();

        String name = bugListDefaultBusinessLineName( bugListDefault );
        if ( name != null ) {
            bugListDefaultDTO.setBusinessLineName( name );
        }
        Long id = bugListDefaultBusinessLineId( bugListDefault );
        if ( id != null ) {
            bugListDefaultDTO.setBusinessLineId( id );
        }
        bugListDefaultDTO.setId( bugListDefault.getId() );
        bugListDefaultDTO.setDescription( bugListDefault.getDescription() );
        bugListDefaultDTO.setStatus( bugListDefault.isStatus() );

        return bugListDefaultDTO;
    }

    @Override
    public BugListDefault toEntity(BugListDefaultDTO bugListDefaultDTO) {
        if ( bugListDefaultDTO == null ) {
            return null;
        }

        BugListDefault bugListDefault = new BugListDefault();

        bugListDefault.setBusinessLine( businessLineMapper.fromId( bugListDefaultDTO.getBusinessLineId() ) );
        bugListDefault.setId( bugListDefaultDTO.getId() );
        bugListDefault.setDescription( bugListDefaultDTO.getDescription() );
        bugListDefault.setStatus( bugListDefaultDTO.isStatus() );

        return bugListDefault;
    }

    private String bugListDefaultBusinessLineName(BugListDefault bugListDefault) {
        if ( bugListDefault == null ) {
            return null;
        }
        BusinessLine businessLine = bugListDefault.getBusinessLine();
        if ( businessLine == null ) {
            return null;
        }
        String name = businessLine.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long bugListDefaultBusinessLineId(BugListDefault bugListDefault) {
        if ( bugListDefault == null ) {
            return null;
        }
        BusinessLine businessLine = bugListDefault.getBusinessLine();
        if ( businessLine == null ) {
            return null;
        }
        Long id = businessLine.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
