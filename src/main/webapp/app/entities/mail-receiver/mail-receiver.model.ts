import { BaseEntity } from './../../shared';

export class MailReceiver implements BaseEntity {
    constructor(
        public id?: number,
        public from?: string,
        public to?: string,
        public status?: boolean,
        public mailSubject?: string,
        public mailId?: number,
    ) {
        this.status = false;
    }
}
