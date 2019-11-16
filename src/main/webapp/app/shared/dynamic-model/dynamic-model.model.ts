export class DynamicModel {
    constructor(
        public name?: any,
        public value?: any
    ) {
        this.name = name ? name : null;
        this.value = value ? value : null;
    }
}
