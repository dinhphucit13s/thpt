import { Component, OnInit } from '@angular/core';
import { FormGroup, ReactiveFormsModule  } from '@angular/forms';
import { FieldConfig } from '../field.interface';
@Component({
    selector: 'jhi-button',
    templateUrl: './button.component.html',
    styles: []
})
export class ButtonComponent implements OnInit {
    group: FormGroup;
    field: FieldConfig;
    constructor() {}
    ngOnInit() {}
}
