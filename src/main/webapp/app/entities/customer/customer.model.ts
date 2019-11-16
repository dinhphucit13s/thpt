import { BaseEntity } from './../../shared';

export const enum CustomerType {
    'KEY',
    ' NORMAL',
    ' POC'
}

export class Customer implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public status?: boolean,
        public description?: any,
        public type?: CustomerType,
    ) {
        this.status = false;
    }
}
