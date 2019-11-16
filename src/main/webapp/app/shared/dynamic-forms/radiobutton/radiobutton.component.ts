import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FieldConfig } from '../field.interface';
@Component({
    selector: 'jhi-radiobutton',
    templateUrl: './radiobutton.component.html',
    styles: []
})
export class RadiobuttonComponent implements OnInit {
    field: FieldConfig;
    group: FormGroup;
    listOptions: string[];
    constructor() {}
    ngOnInit() {
        this.listOptions = new Array<string>();
        this.field.options = this.field.options.toString();
        this.field.options = this.field.options.replace(/[/[/'" ]/g, '');
        this.field.options = this.field.options.replace(/]/g, '');
        this.listOptions = this.field.options.split(',');
        this.field.options = this.listOptions;
    }
}
