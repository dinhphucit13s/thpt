import { BaseEntity } from './../../shared';

export class MembersManagement implements BaseEntity {
    constructor(
        public id?: number,
        public login?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public activated?: boolean,
        public langKey?: string,
        public image?: string,
        public name?: string,
        public status?: boolean,
    ) {
    }
}
