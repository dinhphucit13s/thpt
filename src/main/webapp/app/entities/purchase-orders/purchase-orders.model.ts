import { BaseEntity } from './../../shared';
import {DtmsMonitoring} from '../dtms-monitoring';

export const enum PurchaseOrderStatus {
    'OPEN',
    'PROCESSING',
    'DELIVERED',
    'CANCEL'
}

export class PurchaseOrders implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public status?: PurchaseOrderStatus,
        public startTime?: any,
        public endTime?: any,
        public description?: any,
        public reviewRatio?: any,
        public tmsCustomFieldScreenValues?: BaseEntity[],
        public projectName?: string,
        public projectId?: number,
        public projectTemplatesName?: string,
        public projectTemplatesId?: number,
        public purchaseOrderLeadUserLogin?: string,
        public purchaseOrderLeadId?: number,
        public packages?: BaseEntity[],
        public properties?: BaseEntity[],
        public watcherUsersPO?: string[],
        public dedicatedUsersPO?: string[],
        public dtmsMonitoringProject?: DtmsMonitoring
    ) {
    }
}
