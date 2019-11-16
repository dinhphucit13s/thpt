package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.domain.TMSCustomFieldScreen;
import fpt.dps.dtms.domain.TMSCustomFieldScreenValue;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.service.dto.TMSCustomFieldScreenValueDTO;
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
public class TMSCustomFieldScreenValueMapperImpl implements TMSCustomFieldScreenValueMapper {

    @Autowired
    private PurchaseOrdersMapper purchaseOrdersMapper;
    @Autowired
    private PackagesMapper packagesMapper;
    @Autowired
    private TasksMapper tasksMapper;
    @Autowired
    private TMSCustomFieldScreenMapper tMSCustomFieldScreenMapper;

    @Override
    public List<TMSCustomFieldScreenValue> toEntity(List<TMSCustomFieldScreenValueDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TMSCustomFieldScreenValue> list = new ArrayList<TMSCustomFieldScreenValue>( dtoList.size() );
        for ( TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO : dtoList ) {
            list.add( toEntity( tMSCustomFieldScreenValueDTO ) );
        }

        return list;
    }

    @Override
    public List<TMSCustomFieldScreenValueDTO> toDto(List<TMSCustomFieldScreenValue> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TMSCustomFieldScreenValueDTO> list = new ArrayList<TMSCustomFieldScreenValueDTO>( entityList.size() );
        for ( TMSCustomFieldScreenValue tMSCustomFieldScreenValue : entityList ) {
            list.add( toDto( tMSCustomFieldScreenValue ) );
        }

        return list;
    }

    @Override
    public TMSCustomFieldScreenValueDTO toDto(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        if ( tMSCustomFieldScreenValue == null ) {
            return null;
        }

        TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO = new TMSCustomFieldScreenValueDTO();

        Long id = tMSCustomFieldScreenValueTMSCustomFieldScreenId( tMSCustomFieldScreenValue );
        if ( id != null ) {
            tMSCustomFieldScreenValueDTO.setTmsCustomFieldScreenId( id );
        }
        Long id1 = tMSCustomFieldScreenValuePurchaseOrdersId( tMSCustomFieldScreenValue );
        if ( id1 != null ) {
            tMSCustomFieldScreenValueDTO.setPurchaseOrdersId( id1 );
        }
        Long id2 = tMSCustomFieldScreenValueTasksId( tMSCustomFieldScreenValue );
        if ( id2 != null ) {
            tMSCustomFieldScreenValueDTO.setTasksId( id2 );
        }
        Long id3 = tMSCustomFieldScreenValuePackagesId( tMSCustomFieldScreenValue );
        if ( id3 != null ) {
            tMSCustomFieldScreenValueDTO.setPackagesId( id3 );
        }
        tMSCustomFieldScreenValueDTO.setCreatedBy( tMSCustomFieldScreenValue.getCreatedBy() );
        tMSCustomFieldScreenValueDTO.setCreatedDate( tMSCustomFieldScreenValue.getCreatedDate() );
        tMSCustomFieldScreenValueDTO.setLastModifiedBy( tMSCustomFieldScreenValue.getLastModifiedBy() );
        tMSCustomFieldScreenValueDTO.setLastModifiedDate( tMSCustomFieldScreenValue.getLastModifiedDate() );
        tMSCustomFieldScreenValueDTO.setId( tMSCustomFieldScreenValue.getId() );
        tMSCustomFieldScreenValueDTO.setValue( tMSCustomFieldScreenValue.getValue() );
        tMSCustomFieldScreenValueDTO.setText( tMSCustomFieldScreenValue.getText() );

        return tMSCustomFieldScreenValueDTO;
    }

    @Override
    public TMSCustomFieldScreenValue toEntity(TMSCustomFieldScreenValueDTO tMSCustomFieldScreenValueDTO) {
        if ( tMSCustomFieldScreenValueDTO == null ) {
            return null;
        }

        TMSCustomFieldScreenValue tMSCustomFieldScreenValue = new TMSCustomFieldScreenValue();

        tMSCustomFieldScreenValue.setTMSCustomFieldScreen( tMSCustomFieldScreenMapper.fromId( tMSCustomFieldScreenValueDTO.getTmsCustomFieldScreenId() ) );
        tMSCustomFieldScreenValue.setPackages( packagesMapper.fromId( tMSCustomFieldScreenValueDTO.getPackagesId() ) );
        tMSCustomFieldScreenValue.setPurchaseOrders( purchaseOrdersMapper.fromId( tMSCustomFieldScreenValueDTO.getPurchaseOrdersId() ) );
        tMSCustomFieldScreenValue.setTasks( tasksMapper.fromId( tMSCustomFieldScreenValueDTO.getTasksId() ) );
        tMSCustomFieldScreenValue.setCreatedBy( tMSCustomFieldScreenValueDTO.getCreatedBy() );
        tMSCustomFieldScreenValue.setCreatedDate( tMSCustomFieldScreenValueDTO.getCreatedDate() );
        tMSCustomFieldScreenValue.setLastModifiedBy( tMSCustomFieldScreenValueDTO.getLastModifiedBy() );
        tMSCustomFieldScreenValue.setLastModifiedDate( tMSCustomFieldScreenValueDTO.getLastModifiedDate() );
        tMSCustomFieldScreenValue.setId( tMSCustomFieldScreenValueDTO.getId() );
        tMSCustomFieldScreenValue.setValue( tMSCustomFieldScreenValueDTO.getValue() );
        tMSCustomFieldScreenValue.setText( tMSCustomFieldScreenValueDTO.getText() );

        return tMSCustomFieldScreenValue;
    }

    private Long tMSCustomFieldScreenValueTMSCustomFieldScreenId(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        if ( tMSCustomFieldScreenValue == null ) {
            return null;
        }
        TMSCustomFieldScreen tMSCustomFieldScreen = tMSCustomFieldScreenValue.getTMSCustomFieldScreen();
        if ( tMSCustomFieldScreen == null ) {
            return null;
        }
        Long id = tMSCustomFieldScreen.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long tMSCustomFieldScreenValuePurchaseOrdersId(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        if ( tMSCustomFieldScreenValue == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = tMSCustomFieldScreenValue.getPurchaseOrders();
        if ( purchaseOrders == null ) {
            return null;
        }
        Long id = purchaseOrders.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long tMSCustomFieldScreenValueTasksId(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        if ( tMSCustomFieldScreenValue == null ) {
            return null;
        }
        Tasks tasks = tMSCustomFieldScreenValue.getTasks();
        if ( tasks == null ) {
            return null;
        }
        Long id = tasks.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long tMSCustomFieldScreenValuePackagesId(TMSCustomFieldScreenValue tMSCustomFieldScreenValue) {
        if ( tMSCustomFieldScreenValue == null ) {
            return null;
        }
        Packages packages = tMSCustomFieldScreenValue.getPackages();
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
