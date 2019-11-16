import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FieldConfig } from '../field.interface';
@Component({
    selector: 'jhi-date',
    templateUrl: './date.component.html',
    styles: []
})
export class DateComponent implements OnInit {
    field: FieldConfig;
    group: FormGroup;
    dateFormat = require('dateformat');
    minDate = new Date();
    maxDate = new Date();
    checkValidate: any;
    get isValid() { return this.group.controls[this.field.field].valid; }
    get isDirty() { return this.group.controls[this.field.field].dirty; }
    constructor() {
        this.dateFormat(this.minDate, "dd, mm, yyyy, h:MM:ss TT");
        this.dateFormat(this.maxDate, "dd, mm, yyyy, h:MM:ss TT");
        this.checkValidate = true;
    }
    ngOnInit() {
        if (this.field.validations && this.field.validations.length > 0) {
            this.field.validations.forEach((field) => {
                if (field.name === 'max') {
                    this.maxDate = field.validator;
                    this.checkValidate = false;
                } else if (field.name === 'min') {
                    this.minDate = field.validator;
                    this.checkValidate = false;
                }
            });
            if (this.checkValidate) {
                this.minDate = null;
                this.maxDate = null;
            }
        } else {
            this.minDate = null;
            this.maxDate = null;
        }
    }
}
