package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.service.dto.TasksDTO;
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
public class TasksMapperImpl implements TasksMapper {

    @Autowired
    private PackagesMapper packagesMapper;

    @Override
    public List<Tasks> toEntity(List<TasksDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Tasks> list = new ArrayList<Tasks>( dtoList.size() );
        for ( TasksDTO tasksDTO : dtoList ) {
            list.add( toEntity( tasksDTO ) );
        }

        return list;
    }

    @Override
    public List<TasksDTO> toDto(List<Tasks> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TasksDTO> list = new ArrayList<TasksDTO>( entityList.size() );
        for ( Tasks tasks : entityList ) {
            list.add( toDto( tasks ) );
        }

        return list;
    }

    @Override
    public TasksDTO toDto(Tasks tasks) {
        if ( tasks == null ) {
            return null;
        }

        TasksDTO tasksDTO = new TasksDTO();

        String name = tasksPackagesName( tasks );
        if ( name != null ) {
            tasksDTO.setPackagesName( name );
        }
        Long id = tasksPackagesId( tasks );
        if ( id != null ) {
            tasksDTO.setPackagesId( id );
        }
        tasksDTO.setCreatedBy( tasks.getCreatedBy() );
        tasksDTO.setCreatedDate( tasks.getCreatedDate() );
        tasksDTO.setLastModifiedBy( tasks.getLastModifiedBy() );
        tasksDTO.setLastModifiedDate( tasks.getLastModifiedDate() );
        tasksDTO.setId( tasks.getId() );
        tasksDTO.setName( tasks.getName() );
        tasksDTO.setSeverity( tasks.getSeverity() );
        tasksDTO.setPriority( tasks.getPriority() );
        tasksDTO.setData( tasks.getData() );
        tasksDTO.setFileName( tasks.getFileName() );
        tasksDTO.setType( tasks.getType() );
        tasksDTO.setAvailability( tasks.getAvailability() );
        tasksDTO.setFrame( tasks.getFrame() );
        tasksDTO.setActualObject( tasks.getActualObject() );
        tasksDTO.setOpStatus( tasks.getOpStatus() );
        tasksDTO.setEstimateStartTime( tasks.getEstimateStartTime() );
        tasksDTO.setEstimateEndTime( tasks.getEstimateEndTime() );
        tasksDTO.setOpStartTime( tasks.getOpStartTime() );
        tasksDTO.setOpEndTime( tasks.getOpEndTime() );
        tasksDTO.setReview1Status( tasks.getReview1Status() );
        tasksDTO.setReview1StartTime( tasks.getReview1StartTime() );
        tasksDTO.setReview1EndTime( tasks.getReview1EndTime() );
        tasksDTO.setFixStatus( tasks.getFixStatus() );
        tasksDTO.setFixStartTime( tasks.getFixStartTime() );
        tasksDTO.setFixEndTime( tasks.getFixEndTime() );
        tasksDTO.setReview2Status( tasks.getReview2Status() );
        tasksDTO.setReview2StartTime( tasks.getReview2StartTime() );
        tasksDTO.setReview2EndTime( tasks.getReview2EndTime() );
        tasksDTO.setFiStatus( tasks.getFiStatus() );
        tasksDTO.setFiStartTime( tasks.getFiStartTime() );
        tasksDTO.setFiEndTime( tasks.getFiEndTime() );
        tasksDTO.setDuration( tasks.getDuration() );
        tasksDTO.setTarget( tasks.getTarget() );
        tasksDTO.setErrorQuantity( tasks.getErrorQuantity() );
        tasksDTO.setErrorSeverity( tasks.getErrorSeverity() );
        tasksDTO.setStatus( tasks.getStatus() );
        tasksDTO.setDescription( tasks.getDescription() );
        tasksDTO.setParent( tasks.getParent() );
        tasksDTO.setOp( tasks.getOp() );
        tasksDTO.setReview1( tasks.getReview1() );
        tasksDTO.setReview2( tasks.getReview2() );
        tasksDTO.setFixer( tasks.getFixer() );
        tasksDTO.setFi( tasks.getFi() );

        return tasksDTO;
    }

    @Override
    public Tasks toEntity(TasksDTO tasksDTO) {
        if ( tasksDTO == null ) {
            return null;
        }

        Tasks tasks = new Tasks();

        tasks.setPackages( packagesMapper.fromId( tasksDTO.getPackagesId() ) );
        tasks.setCreatedBy( tasksDTO.getCreatedBy() );
        tasks.setCreatedDate( tasksDTO.getCreatedDate() );
        tasks.setLastModifiedBy( tasksDTO.getLastModifiedBy() );
        tasks.setLastModifiedDate( tasksDTO.getLastModifiedDate() );
        tasks.setId( tasksDTO.getId() );
        tasks.setName( tasksDTO.getName() );
        tasks.setSeverity( tasksDTO.getSeverity() );
        tasks.setPriority( tasksDTO.getPriority() );
        tasks.setData( tasksDTO.getData() );
        tasks.setFileName( tasksDTO.getFileName() );
        tasks.setType( tasksDTO.getType() );
        tasks.setAvailability( tasksDTO.getAvailability() );
        tasks.setFrame( tasksDTO.getFrame() );
        tasks.setActualObject( tasksDTO.getActualObject() );
        tasks.setOpStatus( tasksDTO.getOpStatus() );
        tasks.setEstimateStartTime( tasksDTO.getEstimateStartTime() );
        tasks.setEstimateEndTime( tasksDTO.getEstimateEndTime() );
        tasks.setOpStartTime( tasksDTO.getOpStartTime() );
        tasks.setOpEndTime( tasksDTO.getOpEndTime() );
        tasks.setReview1Status( tasksDTO.getReview1Status() );
        tasks.setReview1StartTime( tasksDTO.getReview1StartTime() );
        tasks.setReview1EndTime( tasksDTO.getReview1EndTime() );
        tasks.setFixStatus( tasksDTO.getFixStatus() );
        tasks.setFixStartTime( tasksDTO.getFixStartTime() );
        tasks.setFixEndTime( tasksDTO.getFixEndTime() );
        tasks.setReview2Status( tasksDTO.getReview2Status() );
        tasks.setReview2StartTime( tasksDTO.getReview2StartTime() );
        tasks.setReview2EndTime( tasksDTO.getReview2EndTime() );
        tasks.setFiStatus( tasksDTO.getFiStatus() );
        tasks.setFiStartTime( tasksDTO.getFiStartTime() );
        tasks.setFiEndTime( tasksDTO.getFiEndTime() );
        tasks.setDuration( tasksDTO.getDuration() );
        tasks.setTarget( tasksDTO.getTarget() );
        tasks.setErrorQuantity( tasksDTO.getErrorQuantity() );
        tasks.setErrorSeverity( tasksDTO.getErrorSeverity() );
        tasks.setStatus( tasksDTO.getStatus() );
        tasks.setDescription( tasksDTO.getDescription() );
        tasks.setParent( tasksDTO.getParent() );
        tasks.setOp( tasksDTO.getOp() );
        tasks.setReview1( tasksDTO.getReview1() );
        tasks.setReview2( tasksDTO.getReview2() );
        tasks.setFixer( tasksDTO.getFixer() );
        tasks.setFi( tasksDTO.getFi() );

        return tasks;
    }

    private String tasksPackagesName(Tasks tasks) {
        if ( tasks == null ) {
            return null;
        }
        Packages packages = tasks.getPackages();
        if ( packages == null ) {
            return null;
        }
        String name = packages.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long tasksPackagesId(Tasks tasks) {
        if ( tasks == null ) {
            return null;
        }
        Packages packages = tasks.getPackages();
        if ( packages == null ) {
            return null;
        }
        Long id = packages.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
