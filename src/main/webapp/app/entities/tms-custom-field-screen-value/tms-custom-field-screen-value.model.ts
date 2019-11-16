import { BaseEntity } from './../../shared';

export class TMSCustomFieldScreenValue implements BaseEntity {
    constructor(
        public id?: number,
        public value?: string,
        public text?: any,
        public purchaseOrdersId?: number,
        public packagesId?: number,
        public tasksId?: number,
        public tmsCustomFieldScreenId?: number,
    ) {
    }
}
