import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { TMSCustomField } from './tms-custom-field.model';
import { TMSCustomFieldService } from './tms-custom-field.service';
import { TMSCustomFieldScreenService } from '../tms-custom-field-screen/tms-custom-field-screen.service';
import {TMSCustomFieldScreen} from '../tms-custom-field-screen/tms-custom-field-screen.model';
import {DynamicPropertiesField, FieldPropertiesConfig} from '../../shared/dynamic-forms/field.interface';

@Injectable()
export class TMSCustomFieldPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private tMSCustomFieldService: TMSCustomFieldService,
        private tMSCustomFieldScreenService: TMSCustomFieldScreenService
    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.tMSCustomFieldService.find(id)
                    .subscribe((tMSCustomFieldResponse: HttpResponse<TMSCustomField>) => {
                        const tMSCustomField: TMSCustomField = tMSCustomFieldResponse.body;
                        const dynamicPropertiesField: DynamicPropertiesField = new DynamicPropertiesField();
                        dynamicPropertiesField.entityPropertiesData = JSON.parse(tMSCustomField.entityData);
                        dynamicPropertiesField.entityPropertiesView = new FieldPropertiesConfig();
                        // tMSCustomField.entity = JSON.parse(tMSCustomField.entity);
                        tMSCustomField.entityData = dynamicPropertiesField;
                        this.ngbModalRef = this.tMSCustomFieldModalRef(component, tMSCustomField);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.tMSCustomFieldModalRef(component, new TMSCustomField());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    openEdit(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.tMSCustomFieldService.find(id)
                    .subscribe((tMSCustomFieldResponse: HttpResponse<TMSCustomField>) => {
                        const tMSCustomField: TMSCustomField = tMSCustomFieldResponse.body;
                        const currentCustomFieldScreen = JSON.parse(localStorage.getItem('currentCustomFieldScreen'));
                        if (currentCustomFieldScreen !== null) {
                            tMSCustomField.entityData = currentCustomFieldScreen.entityGridInput;
                        }
                        this.ngbModalRef = this.tMSCustomFieldModalRef(component, tMSCustomField);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.tMSCustomFieldModalRef(component, new TMSCustomField());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    tMSCustomFieldModalRef(component: Component, tMSCustomField: TMSCustomField): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.tMSCustomField = tMSCustomField;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
