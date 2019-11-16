package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.BusinessLine;
import fpt.dps.dtms.domain.ProjectTemplates;
import fpt.dps.dtms.service.dto.ProjectTemplatesDTO;
import java.util.ArrayList;
import java.util.Arrays;
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
public class ProjectTemplatesMapperImpl implements ProjectTemplatesMapper {

    @Autowired
    private BusinessLineMapper businessLineMapper;

    @Override
    public List<ProjectTemplates> toEntity(List<ProjectTemplatesDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ProjectTemplates> list = new ArrayList<ProjectTemplates>( dtoList.size() );
        for ( ProjectTemplatesDTO projectTemplatesDTO : dtoList ) {
            list.add( toEntity( projectTemplatesDTO ) );
        }

        return list;
    }

    @Override
    public List<ProjectTemplatesDTO> toDto(List<ProjectTemplates> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ProjectTemplatesDTO> list = new ArrayList<ProjectTemplatesDTO>( entityList.size() );
        for ( ProjectTemplates projectTemplates : entityList ) {
            list.add( toDto( projectTemplates ) );
        }

        return list;
    }

    @Override
    public ProjectTemplatesDTO toDto(ProjectTemplates projectTemplates) {
        if ( projectTemplates == null ) {
            return null;
        }

        ProjectTemplatesDTO projectTemplatesDTO = new ProjectTemplatesDTO();

        String name = projectTemplatesBusinessLineName( projectTemplates );
        if ( name != null ) {
            projectTemplatesDTO.setBusinessLineName( name );
        }
        Long id = projectTemplatesBusinessLineId( projectTemplates );
        if ( id != null ) {
            projectTemplatesDTO.setBusinessLineId( id );
        }
        projectTemplatesDTO.setCreatedBy( projectTemplates.getCreatedBy() );
        projectTemplatesDTO.setCreatedDate( projectTemplates.getCreatedDate() );
        projectTemplatesDTO.setLastModifiedBy( projectTemplates.getLastModifiedBy() );
        projectTemplatesDTO.setLastModifiedDate( projectTemplates.getLastModifiedDate() );
        projectTemplatesDTO.setId( projectTemplates.getId() );
        projectTemplatesDTO.setName( projectTemplates.getName() );
        byte[] image = projectTemplates.getImage();
        if ( image != null ) {
            projectTemplatesDTO.setImage( Arrays.copyOf( image, image.length ) );
        }
        projectTemplatesDTO.setImageContentType( projectTemplates.getImageContentType() );
        projectTemplatesDTO.setDescription( projectTemplates.getDescription() );

        return projectTemplatesDTO;
    }

    @Override
    public ProjectTemplates toEntity(ProjectTemplatesDTO projectTemplatesDTO) {
        if ( projectTemplatesDTO == null ) {
            return null;
        }

        ProjectTemplates projectTemplates = new ProjectTemplates();

        projectTemplates.setBusinessLine( businessLineMapper.fromId( projectTemplatesDTO.getBusinessLineId() ) );
        projectTemplates.setCreatedBy( projectTemplatesDTO.getCreatedBy() );
        projectTemplates.setCreatedDate( projectTemplatesDTO.getCreatedDate() );
        projectTemplates.setLastModifiedBy( projectTemplatesDTO.getLastModifiedBy() );
        projectTemplates.setLastModifiedDate( projectTemplatesDTO.getLastModifiedDate() );
        projectTemplates.setId( projectTemplatesDTO.getId() );
        projectTemplates.setName( projectTemplatesDTO.getName() );
        byte[] image = projectTemplatesDTO.getImage();
        if ( image != null ) {
            projectTemplates.setImage( Arrays.copyOf( image, image.length ) );
        }
        projectTemplates.setImageContentType( projectTemplatesDTO.getImageContentType() );
        projectTemplates.setDescription( projectTemplatesDTO.getDescription() );

        return projectTemplates;
    }

    private String projectTemplatesBusinessLineName(ProjectTemplates projectTemplates) {
        if ( projectTemplates == null ) {
            return null;
        }
        BusinessLine businessLine = projectTemplates.getBusinessLine();
        if ( businessLine == null ) {
            return null;
        }
        String name = businessLine.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long projectTemplatesBusinessLineId(ProjectTemplates projectTemplates) {
        if ( projectTemplates == null ) {
            return null;
        }
        BusinessLine businessLine = projectTemplates.getBusinessLine();
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
