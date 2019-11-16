import {BaseEntity} from '../../shared';

export const enum ValueReport {
    Packages_Late = 'Packages_Late',
    Tasks_Late = 'Tasks_Late',
    TasksUnAssign = 'Tasks Un-Assign'
}

export class CustomReports implements BaseEntity {
    constructor(
        public id?: number,
        public userLogin?: string,
        public pageName?: string,
        public value?: any,
    ) {
    }
}
