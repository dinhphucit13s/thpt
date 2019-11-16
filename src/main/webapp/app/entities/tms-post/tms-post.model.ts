import { BaseEntity } from './../../shared';

export class TmsPost implements BaseEntity {
    constructor(
        public id?: any,
        public content?: any,
        public comments?: any,
        public threadId?: any,
        public attachments?: any,
        public attachmentsAppend?: any,
        public attachmentsRemove?: any,
        public commentsDTOs?: any
    ) {
    }
}
