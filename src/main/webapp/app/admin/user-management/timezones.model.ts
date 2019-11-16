import { BaseEntity } from '../../shared';

export class TimeZones implements BaseEntity {
    constructor(
        public id?: string,
        public content?: string,
        public timeZoneContent?: string,
        public timeZoneId?: string
    ) {
    }
}
