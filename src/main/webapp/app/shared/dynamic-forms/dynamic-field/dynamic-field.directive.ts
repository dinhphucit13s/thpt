import {
    ComponentFactoryResolver,
    ComponentRef,
    Directive,
    Input,
    OnInit,
    ViewContainerRef
} from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FieldConfig } from '../field.interface';
import { InputComponent } from '../input/input.component';
import { TextareaComponent } from '../textarea/textarea.component';
import { ButtonComponent } from '../button/button.component';
import { SelectComponent } from '../select/select.component';
import { RadiobuttonComponent } from '../radiobutton/radiobutton.component';
import { CheckboxComponent } from '../checkbox/checkbox.component';
import { DateComponent } from '../date/date.component';

const componentMapper = {
    input: InputComponent,
    textarea: TextareaComponent,
    button: ButtonComponent,
    select: SelectComponent,
    radiobutton: RadiobuttonComponent,
    checkbox: CheckboxComponent,
    date: DateComponent
};
@Directive({
    selector: '[jhiDynamicField]'
})
export class DynamicFieldDirective implements OnInit {
    @Input() field: FieldConfig;
    @Input() group: FormGroup;
    componentRef: any;
    constructor(
        private resolver: ComponentFactoryResolver,
        private container: ViewContainerRef
    ) {}
    ngOnInit() {
        if ((!this.field.hideDynamicField || this.field.hideDynamicField === undefined)
            && this.field.type != null) {
            const factory = this.resolver.resolveComponentFactory(
                componentMapper[this.field.type]
            );
            this.componentRef = this.container.createComponent(factory);
            this.componentRef.instance.field = this.field;
            this.componentRef.instance.group = this.group;
        }
    }
}
