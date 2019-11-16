import { BaseEntity } from './../../shared';

export class BusinessLine implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
    ) {
    }
}
