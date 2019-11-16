import { BaseEntity } from './../../shared';
import {TMSCustomFieldScreen} from '../tms-custom-field-screen';
import {TMSCustomFieldScreenValue} from '../tms-custom-field-screen-value';

export class Packages implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public op?: string,
        public reviewer?: string,
        public fi?: string,
        public delivery?: string,
        public estimateDelivery?: any,
        public target?: number,
        public startTime?: any,
        public endTime?: any,
        public description?: any,
        public tmsCustomFieldScreenValueDTO?: TMSCustomFieldScreenValue[],
        public purchaseOrdersName?: string,
        public purchaseOrdersId?: number,
        public tasks?: BaseEntity[],
        public tmsCustomFieldScreenDTO?: TMSCustomFieldScreen[],
    ) {
        this.tmsCustomFieldScreenValueDTO = tmsCustomFieldScreenValueDTO ? tmsCustomFieldScreenValueDTO : new Array<TMSCustomFieldScreenValue>();
    }
}
