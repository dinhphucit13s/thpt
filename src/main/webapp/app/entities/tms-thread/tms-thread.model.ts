import { BaseEntity } from './../../shared';

export class TmsThread implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public views?: number,
        public answers?: number,
        public closed?: boolean,
        public status?: boolean,
        public projectsName?: string,
        public projectsId?: number,
        public assigneeUserLogin?: string,
        public assigneeId?: number,
    ) {
        this.closed = false;
        this.status = false;
    }
}
