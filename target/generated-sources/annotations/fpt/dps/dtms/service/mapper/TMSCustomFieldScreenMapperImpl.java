package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.ProjectWorkflows;
import fpt.dps.dtms.domain.TMSCustomField;
import fpt.dps.dtms.domain.TMSCustomFieldScreen;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenDTO;
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
public class TMSCustomFieldScreenMapperImpl implements TMSCustomFieldScreenMapper {

    @Autowired
    private TMSCustomFieldMapper tMSCustomFieldMapper;
    @Autowired
    private ProjectWorkflowsMapper projectWorkflowsMapper;

    @Override
    public List<TMSCustomFieldScreen> toEntity(List<TMSCustomFieldScreenDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TMSCustomFieldScreen> list = new ArrayList<TMSCustomFieldScreen>( dtoList.size() );
        for ( TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO : dtoList ) {
            list.add( toEntity( tMSCustomFieldScreenDTO ) );
        }

        return list;
    }

    @Override
    public List<TMSCustomFieldScreenDTO> toDto(List<TMSCustomFieldScreen> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TMSCustomFieldScreenDTO> list = new ArrayList<TMSCustomFieldScreenDTO>( entityList.size() );
        for ( TMSCustomFieldScreen tMSCustomFieldScreen : entityList ) {
            list.add( toDto( tMSCustomFieldScreen ) );
        }

        return list;
    }

    @Override
    public TMSCustomFieldScreenDTO toDto(TMSCustomFieldScreen tMSCustomFieldScreen) {
        if ( tMSCustomFieldScreen == null ) {
            return null;
        }

        TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO = new TMSCustomFieldScreenDTO();

        Long id = tMSCustomFieldScreenProjectWorkflowsId( tMSCustomFieldScreen );
        if ( id != null ) {
            tMSCustomFieldScreenDTO.setProjectWorkflowsId( id );
        }
        String name = tMSCustomFieldScreenProjectWorkflowsName( tMSCustomFieldScreen );
        if ( name != null ) {
            tMSCustomFieldScreenDTO.setProjectWorkflowsName( name );
        }
        Long id1 = tMSCustomFieldScreenTmsCustomFieldId( tMSCustomFieldScreen );
        if ( id1 != null ) {
            tMSCustomFieldScreenDTO.setTmsCustomFieldId( id1 );
        }
        String entityData = tMSCustomFieldScreenTmsCustomFieldEntityData( tMSCustomFieldScreen );
        if ( entityData != null ) {
            tMSCustomFieldScreenDTO.setEntityData( entityData );
        }
        tMSCustomFieldScreenDTO.setCreatedBy( tMSCustomFieldScreen.getCreatedBy() );
        tMSCustomFieldScreenDTO.setCreatedDate( tMSCustomFieldScreen.getCreatedDate() );
        tMSCustomFieldScreenDTO.setLastModifiedBy( tMSCustomFieldScreen.getLastModifiedBy() );
        tMSCustomFieldScreenDTO.setLastModifiedDate( tMSCustomFieldScreen.getLastModifiedDate() );
        tMSCustomFieldScreenDTO.setId( tMSCustomFieldScreen.getId() );
        tMSCustomFieldScreenDTO.setSequence( tMSCustomFieldScreen.getSequence() );
        tMSCustomFieldScreenDTO.setEntityGridInput( tMSCustomFieldScreen.getEntityGridInput() );
        tMSCustomFieldScreenDTO.setEntityGridPm( tMSCustomFieldScreen.getEntityGridPm() );
        tMSCustomFieldScreenDTO.setEntityGridOp( tMSCustomFieldScreen.getEntityGridOp() );

        return tMSCustomFieldScreenDTO;
    }

    @Override
    public TMSCustomFieldScreen toEntity(TMSCustomFieldScreenDTO tMSCustomFieldScreenDTO) {
        if ( tMSCustomFieldScreenDTO == null ) {
            return null;
        }

        TMSCustomFieldScreen tMSCustomFieldScreen = new TMSCustomFieldScreen();

        tMSCustomFieldScreen.setTmsCustomField( tMSCustomFieldMapper.fromId( tMSCustomFieldScreenDTO.getTmsCustomFieldId() ) );
        tMSCustomFieldScreen.setProjectWorkflows( projectWorkflowsMapper.fromId( tMSCustomFieldScreenDTO.getProjectWorkflowsId() ) );
        tMSCustomFieldScreen.setCreatedBy( tMSCustomFieldScreenDTO.getCreatedBy() );
        tMSCustomFieldScreen.setCreatedDate( tMSCustomFieldScreenDTO.getCreatedDate() );
        tMSCustomFieldScreen.setLastModifiedBy( tMSCustomFieldScreenDTO.getLastModifiedBy() );
        tMSCustomFieldScreen.setLastModifiedDate( tMSCustomFieldScreenDTO.getLastModifiedDate() );
        tMSCustomFieldScreen.setId( tMSCustomFieldScreenDTO.getId() );
        tMSCustomFieldScreen.setEntityGridInput( tMSCustomFieldScreenDTO.getEntityGridInput() );
        tMSCustomFieldScreen.setEntityGridPm( tMSCustomFieldScreenDTO.getEntityGridPm() );
        tMSCustomFieldScreen.setEntityGridOp( tMSCustomFieldScreenDTO.getEntityGridOp() );
        tMSCustomFieldScreen.setSequence( tMSCustomFieldScreenDTO.getSequence() );

        return tMSCustomFieldScreen;
    }

    private Long tMSCustomFieldScreenProjectWorkflowsId(TMSCustomFieldScreen tMSCustomFieldScreen) {
        if ( tMSCustomFieldScreen == null ) {
            return null;
        }
        ProjectWorkflows projectWorkflows = tMSCustomFieldScreen.getProjectWorkflows();
        if ( projectWorkflows == null ) {
            return null;
        }
        Long id = projectWorkflows.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String tMSCustomFieldScreenProjectWorkflowsName(TMSCustomFieldScreen tMSCustomFieldScreen) {
        if ( tMSCustomFieldScreen == null ) {
            return null;
        }
        ProjectWorkflows projectWorkflows = tMSCustomFieldScreen.getProjectWorkflows();
        if ( projectWorkflows == null ) {
            return null;
        }
        String name = projectWorkflows.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long tMSCustomFieldScreenTmsCustomFieldId(TMSCustomFieldScreen tMSCustomFieldScreen) {
        if ( tMSCustomFieldScreen == null ) {
            return null;
        }
        TMSCustomField tmsCustomField = tMSCustomFieldScreen.getTmsCustomField();
        if ( tmsCustomField == null ) {
            return null;
        }
        Long id = tmsCustomField.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String tMSCustomFieldScreenTmsCustomFieldEntityData(TMSCustomFieldScreen tMSCustomFieldScreen) {
        if ( tMSCustomFieldScreen == null ) {
            return null;
        }
        TMSCustomField tmsCustomField = tMSCustomFieldScreen.getTmsCustomField();
        if ( tmsCustomField == null ) {
            return null;
        }
        String entityData = tmsCustomField.getEntityData();
        if ( entityData == null ) {
            return null;
        }
        return entityData;
    }
}
