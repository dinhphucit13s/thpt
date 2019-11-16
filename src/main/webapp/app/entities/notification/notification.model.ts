import { BaseEntity } from './../../shared';

export class Notification implements BaseEntity {
    constructor(
        public id?: number,
        public from?: string,
        public to?: string,
        public body?: string,
        public status?: boolean,
    ) {
        this.status = false;
    }
}
