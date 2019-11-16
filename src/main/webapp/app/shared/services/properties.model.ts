export class Properties {
    /*isActiveOnPM?: boolean; // allow display on PM screen?
    isActiveOnOP?: boolean; // allow display on OP screen?
    isRequiredOnPM?: boolean; // always require this field on PM screen
    isRequiredOnOP?: boolean; // always require this field on OP screen
    isRequiredField?: boolean; // is required field?
    isActivityField?: boolean; // is belong to process follow*/
    constructor(
        public isActiveOnPM?: boolean,
        public isActiveOnOP?: boolean,
        public isRequiredOnPM?: boolean,
        public isRequiredOnOP?: boolean,
        public isRequiredField?: boolean,
        public isActivityField?: boolean,
        public isValidField?: boolean,
    ) {
        this.isActiveOnPM = isActiveOnPM ? isActiveOnPM : false;
        this.isActiveOnOP = isActiveOnOP ? isActiveOnOP : false;
        this.isRequiredOnPM = isRequiredOnPM ? isRequiredOnPM : false;
        this.isRequiredOnOP = isRequiredOnOP ? isRequiredOnOP : false;
        this.isRequiredField = isRequiredField ? isRequiredField : false;
        this.isActivityField = isActivityField ? isActivityField : true;
        this.isValidField = isValidField ? isValidField : false;
    }
}
