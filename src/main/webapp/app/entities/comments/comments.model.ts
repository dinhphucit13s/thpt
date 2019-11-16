import { BaseEntity } from './../../shared';

export class Comments implements BaseEntity {
    constructor(
        public id?: number,
        public content?: string,
        public postId?: number,
    ) {
    }
}
