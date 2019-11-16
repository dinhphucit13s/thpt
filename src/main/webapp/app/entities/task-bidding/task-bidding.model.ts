import { BaseEntity } from './../../shared';
import {Tasks} from '../tasks';

export const enum BiddingScope {
    PROJECT = 'PROJECT',
    BU = 'BU',
    PUBLIC = 'PUBLIC'
}

export const enum BiddingStatus {
    NA = 'NA',
    HOLDING = 'HOLDING',
    DOING = 'DOING',
    DONE = 'DONE'
}

export class TaskBidding implements BaseEntity {
    constructor(
        public id?: number,
        public biddingScope?: BiddingScope,
        public biddingStatus?: BiddingStatus,
        public task?: Tasks,
        public biddingRound?: any,
        public pic?: any,
        public startDate?: any,
        public endDate?: any,
        public biddingHoldTime?: any,
        public purchaseOrdersId?: any,
        public purchaseOrdersName?: any,
        public projectId?: any,
        public projectName?: any,
        public teamLead?: string
    ) {
    }
}
