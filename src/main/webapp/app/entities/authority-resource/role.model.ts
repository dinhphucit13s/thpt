import { AuthorityResource } from './authority-resource.model';

export class Role {
    constructor(
        public name?: string,
        public resources?: AuthorityResource[],
    ) {
    }
}