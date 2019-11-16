import { BaseEntity } from './../../shared';
import { Tasks } from '../tasks/tasks.model';

export const enum ProjectRoles {
    'PM',
    'TEAMLEAD',
    'OPERATOR',
    'REVIEWER',
    'FI'
}

export class ProjectUsers implements BaseEntity {
    constructor(
        public id?: number,
        public userLogin?: string,
        public roleName?: ProjectRoles,
        public startDate?: any,
        public endDate?: any,
        public effortPlan?: any,
        public projectName?: string,
        public projectId?: number,
    ) {
    }
}
