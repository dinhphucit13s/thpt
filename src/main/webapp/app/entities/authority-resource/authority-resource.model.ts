import { BaseEntity } from './../../shared';

export class AuthorityResource implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public permission?: number,
        public authorityName?: string,
    ) {
    }
}
