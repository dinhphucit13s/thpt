import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { FieldConfig } from '../field.interface';
@Component({
    selector: 'jhi-input',
    templateUrl: './input.component.html',
    styles: []
})
export class InputComponent implements OnInit {
    field: FieldConfig;
    group: FormGroup;
    get isValid() { return this.group.controls[this.field.field].valid; }
    get isDirty() { return this.group.controls[this.field.field].dirty; }
    constructor() {}
    ngOnInit() {
    }
}
