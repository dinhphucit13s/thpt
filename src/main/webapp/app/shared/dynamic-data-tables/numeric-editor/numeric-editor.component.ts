import {AfterViewInit, Component, ComponentFactoryResolver, ViewChild, ViewContainerRef} from '@angular/core';

import {ICellEditorAngularComp} from 'ag-grid-angular';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Tasks} from '../../../entities/tasks/tasks.model';
import {Observable} from 'rxjs/Observable';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {TasksService} from '../../../entities/tasks/tasks.service';
import {ProjectWorkflowsService} from '../../../entities/project-workflows/project-workflows.service';
import {JhiAlertService, JhiDataUtils, JhiEventManager} from 'ng-jhipster';
import {PackagesService} from '../../../entities/packages';

@Component({
    selector: 'jhi-numeric-cell',
    templateUrl: './numeric-editor.component.html',
})
export class NumericEditorComponent implements ICellEditorAngularComp, AfterViewInit {
    private params: any;
    public value: number;
    tasks: any;
    flag = false;
    dateFormat = require('dateformat');
    private cancelBeforeStart = false;
    purchaseId: number;

    @ViewChild('input', {read: ViewContainerRef}) public input;

    constructor(
        private tasksService: TasksService,
        private eventManager: JhiEventManager,
        public activeModal: NgbActiveModal,
    ) {
    }

    agInit(params: any): void {
        this.tasks = params.data;
        this.params = params;
        this.value = this.params.value;
        // only start edit if key pressed is a number, not a letter
        // this.cancelBeforeStart = params.charPress && ('1234567890'.indexOf(params.charPress) < 0);
        this.cancelBeforeStart = true;
    }

    getValue(): any {
        if (this.flag) {
            this.tasks[this.params.column.colDef.field] = this.value;
            if (this.tasks.id !== undefined) {
                this.subscribeToSaveResponse(
                    this.tasksService.update(this.tasks));
            }
        }
        return this.value;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Tasks>>) {
        result.subscribe((res: HttpResponse<Tasks>) =>
            this.onSaveSuccess(res.body));
    }

    private onSaveSuccess(result: Tasks) {
        this.eventManager.broadcast({ name: 'tasksListModification', content: 'OK'});
        this.activeModal.dismiss(result);
    }

    isCancelBeforeStart(): boolean {
        return this.cancelBeforeStart;
    }

    // will reject the number if it greater than 1,000,000
    // not very practical, but demonstrates the method.
    isCancelAfterEnd(): boolean {
        return this.value > 1000000;
    };

    onKeyDown(event): void {
        if (!this.isKeyPressedNumeric(event)) {
            if (event.preventDefault) event.preventDefault();
        }
    }

    // dont use afterGuiAttached for post gui events - hook into ngAfterViewInit instead for this
    ngAfterViewInit() {
        window.setTimeout(() => {
            this.input.element.nativeElement.focus();
        })
    }

    private getCharCodeFromEvent(event): any {
        event = event || window.event;
        return (typeof event.which === 'undefined') ? event.keyCode : event.which;
    }

    private isCharNumeric(charStr): boolean {
        return !!/\d/.test(charStr);
    }

    private isKeyPressedNumeric(event): boolean {
        const charCode = this.getCharCodeFromEvent(event);
        const charStr = event.key ? event.key : String.fromCharCode(charCode);
        if (this.value === null || this.value !== parseInt(charStr, 10)) {
            this.flag = true;
        }
        return this.isCharNumeric(charStr);
    }
}
