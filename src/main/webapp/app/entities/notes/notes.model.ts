import { BaseEntity } from './../../shared';

export class Notes implements BaseEntity {
    constructor(
        public id?: number,
        public description?: any,
        public attachments?: any[],
        public tasksName?: string,
        public tasksId?: number,
        public bugDescription?: string,
        public bugId?: number,
        public mediaType?: any
    ) {
    }
}
