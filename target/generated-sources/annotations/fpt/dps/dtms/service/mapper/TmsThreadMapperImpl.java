package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.ProjectUsers;
import fpt.dps.dtms.domain.Projects;
import fpt.dps.dtms.domain.TmsPost;
import fpt.dps.dtms.domain.TmsThread;
import fpt.dps.dtms.service.dto.TmsPostDTO;
import fpt.dps.dtms.service.dto.TmsThreadDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:00+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class TmsThreadMapperImpl implements TmsThreadMapper {

    @Autowired
    private ProjectsMapper projectsMapper;
    @Autowired
    private ProjectUsersMapper projectUsersMapper;
    @Autowired
    private TmsPostMapper tmsPostMapper;

    @Override
    public List<TmsThread> toEntity(List<TmsThreadDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TmsThread> list = new ArrayList<TmsThread>( dtoList.size() );
        for ( TmsThreadDTO tmsThreadDTO : dtoList ) {
            list.add( toEntity( tmsThreadDTO ) );
        }

        return list;
    }

    @Override
    public List<TmsThreadDTO> toDto(List<TmsThread> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TmsThreadDTO> list = new ArrayList<TmsThreadDTO>( entityList.size() );
        for ( TmsThread tmsThread : entityList ) {
            list.add( toDto( tmsThread ) );
        }

        return list;
    }

    @Override
    public TmsThreadDTO toDto(TmsThread tmsThread) {
        if ( tmsThread == null ) {
            return null;
        }

        TmsThreadDTO tmsThreadDTO = new TmsThreadDTO();

        Long id = tmsThreadProjectsId( tmsThread );
        if ( id != null ) {
            tmsThreadDTO.setProjectsId( id );
        }
        String userLogin = tmsThreadAssigneeUserLogin( tmsThread );
        if ( userLogin != null ) {
            tmsThreadDTO.setAssigneeUserLogin( userLogin );
        }
        String name = tmsThreadProjectsName( tmsThread );
        if ( name != null ) {
            tmsThreadDTO.setProjectsName( name );
        }
        Long id1 = tmsThreadAssigneeId( tmsThread );
        if ( id1 != null ) {
            tmsThreadDTO.setAssigneeId( id1 );
        }
        tmsThreadDTO.setCreatedBy( tmsThread.getCreatedBy() );
        tmsThreadDTO.setCreatedDate( tmsThread.getCreatedDate() );
        tmsThreadDTO.setLastModifiedBy( tmsThread.getLastModifiedBy() );
        tmsThreadDTO.setLastModifiedDate( tmsThread.getLastModifiedDate() );
        tmsThreadDTO.setId( tmsThread.getId() );
        tmsThreadDTO.setTitle( tmsThread.getTitle() );
        tmsThreadDTO.setViews( tmsThread.getViews() );
        tmsThreadDTO.setAnswers( tmsThread.getAnswers() );
        tmsThreadDTO.setClosed( tmsThread.isClosed() );
        tmsThreadDTO.setStatus( tmsThread.isStatus() );

        return tmsThreadDTO;
    }

    @Override
    public TmsThread toEntity(TmsThreadDTO tmsThreadDTO) {
        if ( tmsThreadDTO == null ) {
            return null;
        }

        TmsThread tmsThread = new TmsThread();

        tmsThread.setProjects( projectsMapper.fromId( tmsThreadDTO.getProjectsId() ) );
        tmsThread.setAssignee( projectUsersMapper.fromId( tmsThreadDTO.getAssigneeId() ) );
        tmsThread.setCreatedBy( tmsThreadDTO.getCreatedBy() );
        tmsThread.setCreatedDate( tmsThreadDTO.getCreatedDate() );
        tmsThread.setLastModifiedBy( tmsThreadDTO.getLastModifiedBy() );
        tmsThread.setLastModifiedDate( tmsThreadDTO.getLastModifiedDate() );
        tmsThread.setPosts( tmsPostDTOSetToTmsPostSet( tmsThreadDTO.getPosts() ) );
        tmsThread.setId( tmsThreadDTO.getId() );
        tmsThread.setTitle( tmsThreadDTO.getTitle() );
        tmsThread.setViews( tmsThreadDTO.getViews() );
        tmsThread.setAnswers( tmsThreadDTO.getAnswers() );
        tmsThread.setClosed( tmsThreadDTO.getClosed() );
        tmsThread.setStatus( tmsThreadDTO.getStatus() );

        return tmsThread;
    }

    private Long tmsThreadProjectsId(TmsThread tmsThread) {
        if ( tmsThread == null ) {
            return null;
        }
        Projects projects = tmsThread.getProjects();
        if ( projects == null ) {
            return null;
        }
        Long id = projects.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String tmsThreadAssigneeUserLogin(TmsThread tmsThread) {
        if ( tmsThread == null ) {
            return null;
        }
        ProjectUsers assignee = tmsThread.getAssignee();
        if ( assignee == null ) {
            return null;
        }
        String userLogin = assignee.getUserLogin();
        if ( userLogin == null ) {
            return null;
        }
        return userLogin;
    }

    private String tmsThreadProjectsName(TmsThread tmsThread) {
        if ( tmsThread == null ) {
            return null;
        }
        Projects projects = tmsThread.getProjects();
        if ( projects == null ) {
            return null;
        }
        String name = projects.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long tmsThreadAssigneeId(TmsThread tmsThread) {
        if ( tmsThread == null ) {
            return null;
        }
        ProjectUsers assignee = tmsThread.getAssignee();
        if ( assignee == null ) {
            return null;
        }
        Long id = assignee.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Set<TmsPost> tmsPostDTOSetToTmsPostSet(Set<TmsPostDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<TmsPost> set1 = new HashSet<TmsPost>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( TmsPostDTO tmsPostDTO : set ) {
            set1.add( tmsPostMapper.toEntity( tmsPostDTO ) );
        }

        return set1;
    }
}
