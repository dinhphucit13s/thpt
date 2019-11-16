package fpt.dps.dtms.service.mapper;

import fpt.dps.dtms.domain.Packages;
import fpt.dps.dtms.domain.PurchaseOrders;
import fpt.dps.dtms.service.dto.PackagesDTO;
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
public class PackagesMapperImpl implements PackagesMapper {

    @Autowired
    private PurchaseOrdersMapper purchaseOrdersMapper;

    @Override
    public List<Packages> toEntity(List<PackagesDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Packages> list = new ArrayList<Packages>( dtoList.size() );
        for ( PackagesDTO packagesDTO : dtoList ) {
            list.add( toEntity( packagesDTO ) );
        }

        return list;
    }

    @Override
    public List<PackagesDTO> toDto(List<Packages> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<PackagesDTO> list = new ArrayList<PackagesDTO>( entityList.size() );
        for ( Packages packages : entityList ) {
            list.add( toDto( packages ) );
        }

        return list;
    }

    @Override
    public PackagesDTO toDto(Packages packages) {
        if ( packages == null ) {
            return null;
        }

        PackagesDTO packagesDTO = new PackagesDTO();

        Long id = packagesPurchaseOrdersId( packages );
        if ( id != null ) {
            packagesDTO.setPurchaseOrdersId( id );
        }
        String name = packagesPurchaseOrdersName( packages );
        if ( name != null ) {
            packagesDTO.setPurchaseOrdersName( name );
        }
        packagesDTO.setCreatedBy( packages.getCreatedBy() );
        packagesDTO.setCreatedDate( packages.getCreatedDate() );
        packagesDTO.setLastModifiedBy( packages.getLastModifiedBy() );
        packagesDTO.setLastModifiedDate( packages.getLastModifiedDate() );
        packagesDTO.setId( packages.getId() );
        packagesDTO.setName( packages.getName() );
        packagesDTO.setOp( packages.getOp() );
        packagesDTO.setReviewer( packages.getReviewer() );
        packagesDTO.setFi( packages.getFi() );
        packagesDTO.setDelivery( packages.getDelivery() );
        packagesDTO.setEstimateDelivery( packages.getEstimateDelivery() );
        packagesDTO.setTarget( packages.getTarget() );
        packagesDTO.setStartTime( packages.getStartTime() );
        packagesDTO.setEndTime( packages.getEndTime() );
        packagesDTO.setDescription( packages.getDescription() );

        return packagesDTO;
    }

    @Override
    public Packages toEntity(PackagesDTO packagesDTO) {
        if ( packagesDTO == null ) {
            return null;
        }

        Packages packages = new Packages();

        packages.setPurchaseOrders( purchaseOrdersMapper.fromId( packagesDTO.getPurchaseOrdersId() ) );
        packages.setCreatedBy( packagesDTO.getCreatedBy() );
        packages.setCreatedDate( packagesDTO.getCreatedDate() );
        packages.setLastModifiedBy( packagesDTO.getLastModifiedBy() );
        packages.setLastModifiedDate( packagesDTO.getLastModifiedDate() );
        packages.setId( packagesDTO.getId() );
        packages.setName( packagesDTO.getName() );
        packages.setOp( packagesDTO.getOp() );
        packages.setReviewer( packagesDTO.getReviewer() );
        packages.setFi( packagesDTO.getFi() );
        packages.setDelivery( packagesDTO.getDelivery() );
        packages.setEstimateDelivery( packagesDTO.getEstimateDelivery() );
        packages.setTarget( packagesDTO.getTarget() );
        packages.setStartTime( packagesDTO.getStartTime() );
        packages.setEndTime( packagesDTO.getEndTime() );
        packages.setDescription( packagesDTO.getDescription() );

        return packages;
    }

    private Long packagesPurchaseOrdersId(Packages packages) {
        if ( packages == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
        if ( purchaseOrders == null ) {
            return null;
        }
        Long id = purchaseOrders.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String packagesPurchaseOrdersName(Packages packages) {
        if ( packages == null ) {
            return null;
        }
        PurchaseOrders purchaseOrders = packages.getPurchaseOrders();
        if ( purchaseOrders == null ) {
            return null;
        }
        String name = purchaseOrders.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
