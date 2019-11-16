import { BaseEntity } from './../../shared';

export const enum IssueStatus {
    'NA',
    'REVIEWING',
    'ACCEPTED',
    'REJECTED',
    'DUPPLICATED'
}

export class Issues implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public status?: IssueStatus,
        public purchaseOrderName?: string,
        public purchaseOrderId?: number,
        public projectsName?: string,
        public projectsId?: number,
        public attachments?: any[],
    ) {
    }
}
