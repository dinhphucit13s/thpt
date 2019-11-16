import { BaseEntity } from './../../shared';

export class ProjectBugListDefault implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public projectName?: string,
        public projectId?: number,
        public bugListDefaultDescription?: string,
        public bugListDefaultId?: number,
    ) {
    }
}
