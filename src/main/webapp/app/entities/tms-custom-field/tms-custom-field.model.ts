import { FieldConfig } from './../../shared/dynamic-forms/field.interface';
export const enum CustomFieldType {
    'STRING',
    'NUMERIC',
    'FLOAT',
    'ENUMERATION',
    'EMAIL',
    'PASSWORD',
    'CHECKBOX',
    'SHORT_DATE',
    'LONG_DATE',
    'RADIOBUTTON',
    'TEXTAREA',
    'LIST',
    'MULTI_SELECTION_LIST'
}
export class TMSCustomField {
    constructor(
        public id?: number,
        public entityData?: any,
    ) {
    }
}
