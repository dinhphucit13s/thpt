import { BaseEntity } from './../../shared';

export class TaskTrackingTime implements BaseEntity {
    constructor(
        public id?: number,
        public taskId?: number,
        public userLogin?: string,
        public role?: string,
        public startTime?: any,
        public endTime?: any,
        public startStatus?: string,
        public endStatus?: string,
        public duration?: number,
    ) {
    }
}
