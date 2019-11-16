import { Component, OnInit, OnDestroy } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Tasks } from './tasks.model';
import { TasksService } from './tasks.service';
import {AppConstants} from '../../shared/services/app-constants';
import {DynamicFieldConfig, FieldConfig} from '../../shared/dynamic-forms/field.interface';
import {TMSCustomFieldScreen} from '../tms-custom-field-screen/tms-custom-field-screen.model';
import {DynamicModel} from '../../shared/dynamic-model/dynamic-model.model';
declare var jquery: any;
declare var $: any;
@Component({
    selector: 'jhi-tasks-detail',
    templateUrl: './tasks-detail.component.html',
    styleUrls: ['./tasks-detail.component.css'],
})
export class TasksDetailComponent implements OnInit, OnDestroy {

    tasks: Tasks;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    strPassRoute = '';
    // model config for CustomField
    dynamicFieldConfig: DynamicFieldConfig;
    displayedColumns: FieldConfig[] = new Array<FieldConfig>();
    tmsCustomFieldScreens: TMSCustomFieldScreen[];

    positionGeneral: FieldConfig[] = new Array<FieldConfig>();
    positionNecessary: FieldConfig[] = new Array<FieldConfig>();
    positionPrivateOR: FieldConfig[] = new Array<FieldConfig>();
    positionPrivateRF: FieldConfig[] = new Array<FieldConfig>();
    formatDateTime: FieldConfig[] = new Array<FieldConfig>();
    _bidding: any;
    count: any;
    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private tasksService: TasksService,
        private route: ActivatedRoute,
        private router: Router,
    ) {
    }

    ngOnInit() {
        this.count = 0;
        this._bidding = this.route.snapshot.queryParams['bidding'];
        this.getAllColumnsDisplay();
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTasks();
    }

    getAllColumnsDisplay() {
        this.formatDateTime = AppConstants.FormatDateTime;
        this.tasksService.loadTaskDynamicColumn(sessionStorage.getItem('currentPurchaseOrderId')).subscribe((res: HttpResponse<any>) => {
            this.dynamicFieldConfig = res.body;
            this.displayedColumns = this.dynamicFieldConfig.fieldConfigVMs;
            this.tmsCustomFieldScreens = this.dynamicFieldConfig.tmsCustomFieldScreenDTOs;
            // add custom field for ag-grid columns
            if (this.tmsCustomFieldScreens && this.tmsCustomFieldScreens.length > 0) {
                this.tmsCustomFieldScreens.forEach((tmsCustomField) => {
                    tmsCustomField.entityGridPm = JSON.parse(tmsCustomField.entityGridPm);
                    if (tmsCustomField.entityGridPm.length > 0) {
                        tmsCustomField.entityData = JSON.parse(tmsCustomField.entityData);
                        tmsCustomField.entityGridInput = JSON.parse(tmsCustomField.entityGridInput);
                        tmsCustomField.entityGridInput = tmsCustomField.entityGridInput[0];
                        const copy: FieldConfig = Object.assign(tmsCustomField.entityData, tmsCustomField.entityGridInput);
                        this.displayedColumns.push(copy);
                    }
                });
            }
            this.displayedColumns.forEach((field) => {
                if (field.format === 'datetime') {
                    this.formatDateTime.forEach((fieldt) => {
                        field.cellRenderer = fieldt.cellRenderer;
                    });
                }
                if (field.displayPosition === 0 && field.headerName !== 'Actions') {
                    this.positionNecessary.push(field);
                } else if (field.displayPosition === 1) {
                    this.positionGeneral.push(field);
                } else if (field.displayPosition === 2) {
                    this.positionPrivateOR.push(field);
                } else if (field.displayPosition === 3) {
                    this.positionPrivateRF.push(field);
                }
            });
            this.sortByPosition(this.positionNecessary);
            this.sortByPosition(this.positionGeneral);
            this.sortByPosition(this.positionPrivateOR);
            this.sortByPosition(this.positionPrivateRF);
        });
    }
    sortByPosition(listData: any) {
        return listData.sort((a, b) => {
            return a.sortByPosition - b.sortByPosition;
        });
    }
    load(id) {
        this.tasksService.find(id)
            .subscribe((tasksResponse: HttpResponse<Tasks>) => {
                this.tasks = tasksResponse.body;
                this.mapperData(this.tasks);
                if (this.tasks !== undefined ) {
                    this.strPassRoute = "{'id':'" + this.tasks.id + "','route':'tasks'}";
                    // this.strPassRoute = `{'id': ${this.tasks.id} ,'route':'tasks'}`;
                }
            });
    }
    showDetailFilePath(params, field: any) {
        if (field.field === 'fileName' || field.field === 'description') {
            if (params) {
                let counts = 0;
                let elem = null;
                const spanContent =  $('span.content');
                if (spanContent.length > 1) {
                    if (field.field === 'description') {
                        elem = spanContent[0];
                    } else {
                        elem = spanContent[1];
                    }
                } else {
                    elem = spanContent[0];
                }
                console.log($(elem).outerWidth());
                console.log($(elem).outerHeight());
                const left = params.clientX - params.offsetX + ($(elem).outerWidth() * 20 / 100) + window.scrollX;
                const top = params.clientY - params.offsetY + $(elem).outerHeight() + window.scrollY + 10;
                const value = params.target.textContent.replace(/___/g, '$');
                const valueArr = value.split('$');
                let inside = '';
                valueArr.forEach((val) => {
                    inside = inside + '<div><a class="tooltip-path">'
                        + '<span class="tooltiptext">Copy to clipboard</span>Copy</a><span>' + val + '</span></div>';
                });
                const html = '<div class="detail-path" style="position: absolute; top:'
                    + top + 'px' + '; left: ' + left + 'px' + '"><span>' + inside + '</span></div>';
                $('body').find('.detail-path').remove();
                $('body').append(html);
                $('body').find('.detail-path').find('a').click(function(event) {
                    const text = $(event.target).next().text();
                    const selBox = document.createElement('textarea');
                    selBox.style.position = 'fixed';
                    selBox.style.left = '0';
                    selBox.style.top = '0';
                    selBox.style.opacity = '0';
                    selBox.value = text;
                    document.body.appendChild(selBox);
                    selBox.focus();
                    selBox.select();
                    document.execCommand('copy');
                    document.body.removeChild(selBox);
                    $('.detail-path').find('a').find('span').text('Copy to clipboard');
                    $(event.target).find('span').text('Copied');
                });
                $('body').click(function() {
                    if (counts > 0) {
                        $('.detail-path').remove();
                        $('body').prop('onclick', null).off('click');
                        counts = 0;
                    }
                    counts++;
                });
                $('.detail-path').click(function(event) {
                    event.stopPropagation();
                });
            }
        }
    }

    // Create model for new field, add to rowData.
    private mapperData(rowData: any) {
        this.displayedColumns.forEach((item) => {
            const index = this.tmsCustomFieldScreens.findIndex((field) => field.entityData.field === item.field);
            if (index >= 0) {
                if (rowData.tmsCustomFieldScreenValueDTO !== undefined && rowData.tmsCustomFieldScreenValueDTO !== null) {
                    const indexValue = rowData.tmsCustomFieldScreenValueDTO.findIndex((value) => value.tmsCustomFieldScreenId === this.tmsCustomFieldScreens[index].id);
                    if (indexValue >= 0) {
                        const dynamicModel: DynamicModel = new DynamicModel();
                        let row: any;
                        dynamicModel.name = item.field;
                        if (rowData.tmsCustomFieldScreenValueDTO[indexValue].value !== null) {
                            dynamicModel.value = rowData.tmsCustomFieldScreenValueDTO[indexValue].value;
                        } else {
                            dynamicModel.value = rowData.tmsCustomFieldScreenValueDTO[indexValue].text;
                        }
                        row = {
                            [dynamicModel.name]: dynamicModel.value
                        };
                        Object.assign(rowData, row);
                    }
                }
            }
        });
    }
    onClickCloneTask(taskId) {
        const name = 'clone';
        this.router.navigate(['/', { outlets: { popup: 'derived/' + 'tasks/' + taskId + '/' + name} }]);
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTasks() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tasksListModification',
            (response) => this.load(this.tasks.id)
        );
    }
}
