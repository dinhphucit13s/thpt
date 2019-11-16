import { BaseEntity } from './../../shared';

export class TMSCustomFieldScreen implements BaseEntity {
    constructor(
        public id?: number,
        public sequence?: number,
        public entityGridInput?: any,
        public entityGridPm?: any,
        public entityGridOp?: any,
        public entityData?: any,
        public tmsCustomFieldId?: number,
        public projectWorkflowsName?: string,
        public projectWorkflowsId?: number,
    ) {
    }
}
