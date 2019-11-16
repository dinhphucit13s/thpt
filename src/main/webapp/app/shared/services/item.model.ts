import { Properties } from './properties.model';

export class Item {
    constructor(
        public position?: string,
        public name?: string,
        public label?: string,
        public inputType?: string,
        public option?: string[],
        public collections?: string[],
        public type?: string,
        public value?: any,
        public validation?: Validator[],
        public properties?: Properties,
    ) {
    }
}

export interface Validator {
    name: string;
    validator: any;
    message: string;
}
