import { BaseEntity } from './../../shared';

export class Attachments implements BaseEntity {
    constructor(
        public id?: number,
        public filename?: string,
        public diskFile?: string,
        public fileType?: string,
        public bugsDescription?: string,
        public bugsId?: number,
        public notesDescription?: string,
        public notesId?: number,
        public issuesName?: string,
        public issuesId?: number,
        public mailId?: number,
        public commentId?: number,
        public postId?: number,
    ) {
    }
}
