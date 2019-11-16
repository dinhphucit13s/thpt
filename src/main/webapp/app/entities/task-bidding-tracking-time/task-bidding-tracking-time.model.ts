import { BaseEntity } from './../../shared';

export const enum BiddingScope {
    'NA',
    ' BU',
    ' PUBLIC'
}

export class TaskBiddingTrackingTime implements BaseEntity {
    constructor(
        public id?: number,
        public userLogin?: string,
        public role?: string,
        public startTime?: any,
        public endTime?: any,
        public startStatus?: string,
        public endStatus?: string,
        public duration?: number,
        public biddingScope?: BiddingScope,
        public taskName?: string,
        public taskId?: number,
    ) {
    }
}
