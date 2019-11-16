import { AuthorityResource } from '../../entities/authority-resource/authority-resource.model';

export class Permission {
    constructor(
        public username?: string,
        public resources?: AuthorityResource[],
    ) {
    }
}