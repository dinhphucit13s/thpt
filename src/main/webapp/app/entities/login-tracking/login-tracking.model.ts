import { BaseEntity } from './../../shared';

export class LoginTracking implements BaseEntity {
    constructor(
        public id?: number,
        public login?: string,
        public startTime?: any,
        public endTime?: any,
    ) {
    }
}
