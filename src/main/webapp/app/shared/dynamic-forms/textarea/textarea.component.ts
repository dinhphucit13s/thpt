import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FieldConfig } from '../field.interface';
@Component({
    selector: 'jhi-textarea',
    templateUrl: './textarea.component.html'
})
export class TextareaComponent implements OnInit {
    field: FieldConfig;
    group: FormGroup;
    get isValid() { return this.group.controls[this.field.field].valid; }
    get isDirty() { return this.group.controls[this.field.field].dirty; }
    constructor() {}
    ngOnInit() {
        const fieldOld = this.field;
    }
}
