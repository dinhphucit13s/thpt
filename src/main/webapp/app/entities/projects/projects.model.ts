import { BaseEntity } from './../../shared';

export const enum ProjectType {
    'NA',
    'MAINTENANCE',
    'BODY_SHOPPING'
}

export const enum ProjectStatus {
    'OPEN',
    'RUNNING',
    'CLOSED'
}

export class Projects implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public type?: ProjectType,
        public status?: ProjectStatus,
        public biddingHoldTime?: any,
        public startTime?: any,
        public endTime?: any,
        public description?: any,
        public hasDoingTask?: any,
        public projectTemplatesName?: string,
        public projectTemplatesId?: number,
        public projectLeadUserLogin?: string,
        public projectLeadId?: number,
        public purchaseOrders?: BaseEntity[],
        public projectUsers?: BaseEntity[],
        public bugListDefaults?: BaseEntity[],
        public customerName?: string,
        public customerId?: number,
        public businessUnitName?: string,
        public businessUnitId?: number,
        public watcherUsers?: string[],
        public dedicatedUsers?: string[]
    ) {
    }
}
