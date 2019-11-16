import {
    Component,
    EventEmitter,
    Input,
    OnChanges,
    OnInit,
    Output
} from '@angular/core';
import {
    FormGroup,
    FormBuilder,
    Validators,
    FormControl
} from '@angular/forms';
import { FieldConfig, Validator } from '../field.interface';
import {AppConstants} from '../../services/app-constants';

@Component({
    exportAs: 'dynamicForm',
    selector: 'jhi-dynamic-form',
    templateUrl: './dynamic-form.component.html',
    styles: []
})
export class DynamicFormComponent implements OnInit {
    @Input() fields: FieldConfig[];

    @Output() submit: EventEmitter<any> = new EventEmitter<any>();

    form: FormGroup = new FormGroup({});

    get value() {
        return this.form.value;
    }
    constructor(private fb: FormBuilder) {}

    ngOnInit() {
        this.form = this.createControl();
    }
    createControl() {
        const group = this.fb.group({});
        this.fields.forEach((field) => {
            if (field.type === 'button') {
                return;
            }
            if (field.properties.isRequiredField) {
                const requiredValid: Validator = new Validator();
                requiredValid.name = 'required';
                requiredValid.message = 'required';
                requiredValid.validator = this.requiredValidate();
                if (field.validations) {
                    field.validations.push(requiredValid);
                } else {
                    field.validations = new Array<Validator>();
                    field.validations.push(requiredValid);
                }
            }
            if (field.validations && field.validations.length > 0) {
                field.validations.forEach((validate) => {
                    if (validate.name === 'pattern') {
                        validate.validator = this.patternValidate(validate.validator);
                    }
                    if (validate.name === 'maxlength') {
                        validate.validator = this.maxLengthValidate(validate.validator);
                    }
                    if (validate.name === 'minlength') {
                        validate.validator = this.minLengthValidate(validate.validator);
                    }
                    if (validate.name === 'max' && field.type !== 'date') {
                        validate.validator = this.maxValidate(validate.validator);
                    }
                    if (validate.name === 'min' && field.type !== 'date') {
                        validate.validator = this.minValidate(validate.validator);
                    }
                });
            }
            if (field.validations && field.validations.length > 0) {
                const control = this.fb.control(
                    field.value,
                    this.bindValidations(field.validations || [])
                );
                group.addControl(field.field, control);
            } else {
                const control = this.fb.control(
                    field.value
                );
                group.addControl(field.field, control);
            }
        });
        return group;
    }

    bindValidations(validations: any) {
        if (validations.length > 0) {
            const validList = [];
            validations.forEach((valid) => {
                if (typeof valid.validator !== 'string') {
                    validList.push(valid.validator);
                }
            });
            return Validators.compose(validList);
        }
        return null;
    }
    requiredValidate() {
        return Validators.required;
    }
    patternValidate(params) {
        return Validators.pattern(params);
    }
    maxLengthValidate(params) {
        return Validators.maxLength(params);
    }
    minLengthValidate(params) {
        return Validators.minLength(params);
    }
    maxValidate(params) {
        return Validators.max(params);
    }
    minValidate(params) {
        return Validators.min(params);
    }
}
