import { BaseEntity } from './../../shared';

export class TMSLogHistory implements BaseEntity {
    constructor(
        public id?: number,
        public action?: string,
        public oldValue?: any,
        public newValue?: any,
        public projectsName?: string,
        public projectsId?: number,
        public purchaseOrdersName?: string,
        public purchaseOrdersId?: number,
        public packagesName?: string,
        public packagesId?: number,
        public tasksName?: string,
        public tasksId?: number,
    ) {
    }
}
