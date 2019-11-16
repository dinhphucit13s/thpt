import { BaseEntity } from './../../shared';

export class BusinessUnitManager implements BaseEntity {
    constructor(
        public id?: number,
        public startTime?: any,
        public endTime?: any,
        public description?: string,
        public businessUnitName?: string,
        public businessUnitId?: number,
        public managerLogin?: string,
        public managerId?: number,
    ) {
    }
}
