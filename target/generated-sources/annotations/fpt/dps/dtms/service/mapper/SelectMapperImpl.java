package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.service.dto.SelectDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-10-04T10:12:00+0700",
    comments = "version: 1.2.0.Final, compiler: Eclipse JDT (IDE) 1.2.100.v20160418-1457, environment: Java 1.8.0_111 (Oracle Corporation)"
)
@Component
public class SelectMapperImpl implements SelectMapper {

    @Override
    public PurchaseOrders toEntity(SelectDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PurchaseOrders purchaseOrders = new PurchaseOrders();

        purchaseOrders.setId( dto.getId() );
        purchaseOrders.setName( dto.getName() );

        return purchaseOrders;
    }

    @Override
    public List<PurchaseOrders> toEntity(List<SelectDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<PurchaseOrders> list = new ArrayList<PurchaseOrders>( dtoList.size() );
        for ( SelectDTO selectDTO : dtoList ) {
            list.add( toEntity( selectDTO ) );
        }

        return list;
    }

    @Override
    public List<SelectDTO> toDto(List<PurchaseOrders> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SelectDTO> list = new ArrayList<SelectDTO>( entityList.size() );
        for ( PurchaseOrders purchaseOrders : entityList ) {
            list.add( toDto( purchaseOrders ) );
        }

        return list;
    }

    @Override
    public SelectDTO toDto(PurchaseOrders purchaseOrders) {
        if ( purchaseOrders == null ) {
            return null;
        }

        SelectDTO selectDTO = new SelectDTO();

        selectDTO.setId( purchaseOrders.getId() );
        selectDTO.setName( purchaseOrders.getName() );

        return selectDTO;
    }
}
