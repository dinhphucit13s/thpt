import { BaseEntity } from './../../shared';

export class BugListDefault implements BaseEntity {
    constructor(
        public id?: number,
        public description?: any,
        public status?: boolean,
        public businessLineName?: string,
        public businessLineId?: number,
    ) {
        this.status = false;
    }
}
