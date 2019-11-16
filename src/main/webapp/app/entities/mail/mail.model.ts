import { BaseEntity } from './../../shared';

export class Mail implements BaseEntity {
    constructor(
        public id?: number,
        public from?: any,
        public subject?: string,
        public body?: string,
        public startTime?: any,
        public endTime?: any,
        public attachments?: any[],
    ) {
    }
}
