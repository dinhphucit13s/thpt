import { BaseEntity } from './../../shared';

export class ProjectTemplates implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public imageContentType?: string,
        public image?: any,
        public description?: any,
        public businessLineName?: string,
        public businessLineId?: number,
        public projects?: BaseEntity[],
    ) {
    }
}
