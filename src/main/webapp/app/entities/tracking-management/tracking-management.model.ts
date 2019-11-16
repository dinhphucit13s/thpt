import { BaseEntity } from './../../shared';
import { ProjectUsers } from '../project-users/project-users.model';

export const enum TaskSeverity {
    'NORMAL',
    'MEDIUM',
    'HIGH'
}

export const enum TaskPriority {
    'NORMAL',
    'MEDIUM',
    'HIGH'
}

export const enum TaskAvailability {
    'NA',
    'OK',
    'NO_OBJECT',
    'NO_SCOPE',
    'REWORK'
}

export const enum OPStatus {
    'NA',
    'OPEN',
    'DOING',
    'DONE',
    'CANCEL',
    'REOPEN'
}

export const enum ReviewStatus {
    'NA',
    'OPEN',
    'DOING',
    'DONE',
    'NOT_GOOD',
    'REOPEN'
}

export const enum FixStatus {
    'NA',
    'OPEN',
    'DOING',
    'DONE',
    'REOPEN'
}

export const enum FIStatus {
    'NA',
    'OPEN',
    'DOING',
    'DONE',
    'NOT_GOOD'
}

export const enum ErrorSeverity {
    'NA',
    'TRIVIAL',
    'MINOR',
    'MAJOR',
    'CRITICAL'
}

export const enum TaskStatus {
    'NA',
    'OPEN',
    'DOING',
    'DONE',
    'CANCEL',
    'PENDING',
    'CLOSED'
}

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

export class TrackingManagement implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public severity?: TaskSeverity,
        public priority?: TaskPriority,
        public data?: string,
        public fileName?: string,
        public type?: string,
        public availability?: TaskAvailability,
        public frame?: number,
        public actualObject?: number,
        public opStatus?: OPStatus,
        public estimateStartTime?: any,
        public estimateEndTime?: any,
        public opStartTime?: any,
        public opEndTime?: any,
        public review1Status?: ReviewStatus,
        public review1StartTime?: any,
        public review1EndTime?: any,
        public fixStatus?: FixStatus,
        public fixStartTime?: any,
        public fixEndTime?: any,
        public review2Status?: ReviewStatus,
        public review2StartTime?: any,
        public review2EndTime?: any,
        public fiStatus?: FIStatus,
        public fiStartTime?: any,
        public fiEndTime?: any,
        public duration?: number,
        public target?: number,
        public errorQuantity?: number,
        public errorSeverity?: ErrorSeverity,
        public status?: TaskStatus,
        public description?: any,
        public parent?: number,
        public op?: string,
        public review1?: string,
        public review2?: string,
        public fixer?: string,
        public fi?: string,
        public tmsCustomFieldScreenValues?: BaseEntity[],
        public packagesName?: string,
        public packagesId?: number,
    ) {
    }
}
