import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { FieldConfig } from '../field.interface';
import { SelectService } from './select.service';
import { JhiAlertService } from 'ng-jhipster';
import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {ReuseSelect, Selects} from './select.model';
import {DataService} from '../../services/data.service';

@Component({
    selector: 'jhi-select',
    templateUrl: './select.component.html',
    providers: [ SelectService ]
})
export class SelectComponent implements OnInit {
    field: FieldConfig;
    group: FormGroup;
    items: Selects[];
    oldData: ReuseSelect[] = new Array<ReuseSelect>();
    itemData: ReuseSelect = new ReuseSelect();
    dropdownSettings: any;

    listOptions: string[];
    listResult: string[];
    list: any;
    get isValid() { return this.group.controls[this.field.field].valid; }
    get isDirty() { return this.group.controls[this.field.field].dirty; }
    constructor(
        private selectService: SelectService,
        private jhiAlertService: JhiAlertService,
        private dataService: DataService,
    ) {
        this.listResult = new Array<string>();
    }
    ngOnInit() {
        this.listOptions = new Array<string>();
        this.dropdownSettings = {
            singleSelection: false,
            idField: 'field',
            textField: 'headerName',
            itemsShowLimit: 2,
            allowSearchFilter: true,
            enableCheckAll: false,
            limitSelection: 1000000
        };
        if (this.field.options === null || this.field.options === undefined) {
            if (this.field.reuseData === undefined || this.field.reuseData === null) {
                this.selectService.query({query: sessionStorage.getItem('currentPurchaseOrderId')}, this.field.urlOption)
                    .subscribe((res: HttpResponse<any[]>) => {
                            this.items = res.body;
                            const reusedData = this.dataService.getReusedData().getValue();
                            this.itemData.name = this.field.field;
                            this.itemData.select = this.items;
                            reusedData.push(this.itemData);
                            this.dataService.changeReusedData(reusedData);
                            this.listOptions = new Array<string>();
                            this.items.forEach((select) => {
                                this.list = JSON.parse(JSON.stringify(select));
                                this.listOptions.push(this.list['name']);
                                this.field.options = this.listOptions;
                            });
                        },
                        (res: HttpErrorResponse) => this.onError(res.message));
            } else {
                this.dataService.reusedDataSub.subscribe((oldData) => {
                    oldData.forEach((data) => {
                        if (data.name === this.field.reuseData) {
                            this.listOptions = new Array<string>();
                            this.items = data.select;
                            this.items.forEach((select) => {
                                this.list = JSON.parse(JSON.stringify(select));
                                this.listOptions.push(this.list['name']);
                                this.field.options = this.listOptions;
                            });
                        }
                    });
                });
            }
        } else {
            this.field.options = this.field.options.toString();
            this.field.options = this.field.options.replace(/[/[/'" ]/g, '');
            this.field.options = this.field.options.replace(/]/g, '');
            this.listOptions = this.field.options.split(',');
            this.field.options = this.listOptions;
        }
    }
    onItemSelect(item: any) {
        this.listResult.push(item);
        this.field.field = this.listResult.toString();
    }

    onDeSelect(item: any) {
        const index = this.listResult.findIndex((x) => x === item);
        this.listResult.splice(index, 1);
        this.field.field = this.listResult.toString();
    }
    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
