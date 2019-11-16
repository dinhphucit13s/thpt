import { BaseEntity } from './../../shared';
import {TMSCustomFieldScreen} from '../tms-custom-field-screen';
import {TMSCustomFieldScreenValue} from '../tms-custom-field-screen-value';

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
    NA = 'NA',
    OPEN = 'OPEN',
    DOING = 'DOING',
    PENDING = 'PENDING',
    DONE = 'DONE',
    CANCEL = 'CANCEL',
    REOPEN = 'REOPEN',
    RETURN  = 'RETURN'
}

export const enum ReviewStatus {
    NA = 'NA',
    OPEN = 'OPEN',
    DOING = 'DOING',
    PENDING = 'PENDING',
    DONE = 'DONE',
    NOT_GOOD = 'NOT_GOOD',
    REOPEN = 'REOPEN'
}

export const enum FixStatus {
    NA = 'NA',
    OPEN = 'OPEN',
    DOING = 'DOING',
    PENDING = 'PENDING',
    DONE = 'DONE',
    CANCEL = 'CANCEL',
    REOPEN = 'REOPEN',
    RE_ASSIGN = 'RE_ASSIGN',
    RE_ASSIGN_REVIEW1 = 'RE_ASSIGN_REVIEW1',
}

export const enum FIStatus {
    NA = 'NA',
    OPEN = 'OPEN',
    DOING = 'DOING',
    PENDING = 'PENDING',
    DONE = 'DONE',
    NOT_GOOD = 'NOT_GOOD'
}

export const enum ErrorSeverity {
    'NA',
    'TRIVIAL',
    'MINOR',
    'MAJOR',
    'CRITICAL'
}

export const enum TaskStatus {
    NA = 'NA',
    OPEN = 'OPEN',
    DOING = 'DOING',
    DONE = 'DONE',
    CANCEL = 'CANCEL',
    PENDING = 'PENDING',
    CLOSED = 'CLOSED'
}

export class Tasks implements BaseEntity {
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
        public duration?: any,
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
        public tmsCustomFieldScreenDTO?: TMSCustomFieldScreen[],
        public tmsCustomFieldScreenValueDTO?: TMSCustomFieldScreenValue[],
    ) {
        this.tmsCustomFieldScreenValueDTO = tmsCustomFieldScreenValueDTO ? tmsCustomFieldScreenValueDTO : new Array<TMSCustomFieldScreenValue>();
    }
}
