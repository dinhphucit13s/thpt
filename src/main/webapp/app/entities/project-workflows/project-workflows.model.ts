import { BaseEntity } from './../../shared';
import {TMSCustomField} from '../tms-custom-field';
import {TMSCustomFieldScreen} from '../tms-custom-field-screen';

export class ProjectWorkflows implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public step?: number,
        public entityDTO?: any,
        public inputDTO?: any,
        public opGridDTO?: any,
        public pmGridDTO?: any,
        public nextURI?: string,
        public description?: any,
        public activity?: any,
        public projectTemplatesName?: string,
        public projectTemplatesId?: number,
        public tmsCustomFields?: TMSCustomFieldScreen[]
    ) {
    }
}

export class TaskWorkflowVM {
    constructor(
        public processKey?: string,
        public image?: string,
    ) {
    }
}
