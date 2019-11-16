import { BaseEntity } from './../../shared';

export const enum BugStatus {
    'OPEN',
    ' FIXING',
    ' CLOSED',
    ' REOPEN',
    ' CANCELLED',
    ' PENDING'
}

export const enum BugResolution {
    'NA',
    ' FIXED',
    ' WILL_NOT_FIX',
    ' CAN_NOT_REPRODUCE',
    ' CANCEL'
}

export class Bugs implements BaseEntity {
    constructor(
        public id?: number,
        public descriptionContentType?: string,
        public description?: any,
        public code?: string,
        public iteration?: number,
        public stage?: string,
        public status?: BugStatus,
        public resolution?: BugResolution,
        public physicalPath?: string,
        public tasksName?: string,
        public tasksId?: number,
        public notes?: BaseEntity[],
        public mediaType?: any,
        public attachments?: any[]
    ) {
    }
}
