import { BaseEntity } from './../../shared';

export const enum NotificationCategory {
    'TASK'
}

export class NotificationTemplate implements BaseEntity {
    constructor(
        public id?: number,
        public type?: NotificationCategory,
        public template?: string,
        public description?: string,
    ) {
    }
}
