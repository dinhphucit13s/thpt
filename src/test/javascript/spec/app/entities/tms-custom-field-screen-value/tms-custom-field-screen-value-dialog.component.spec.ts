/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldScreenValueDialogComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value-dialog.component';
import { TMSCustomFieldScreenValueService } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value.service';
import { TMSCustomFieldScreenValue } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value.model';
import { PurchaseOrdersService } from '../../../../../../main/webapp/app/entities/purchase-orders';
import { PackagesService } from '../../../../../../main/webapp/app/entities/packages';
import { TasksService } from '../../../../../../main/webapp/app/entities/tasks';
import { TMSCustomFieldService } from '../../../../../../main/webapp/app/entities/tms-custom-field';

describe('Component Tests', () => {

    describe('TMSCustomFieldScreenValue Management Dialog Component', () => {
        let comp: TMSCustomFieldScreenValueDialogComponent;
        let fixture: ComponentFixture<TMSCustomFieldScreenValueDialogComponent>;
        let service: TMSCustomFieldScreenValueService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldScreenValueDialogComponent],
                providers: [
                    PurchaseOrdersService,
                    PackagesService,
                    TasksService,
                    TMSCustomFieldService,
                    TMSCustomFieldScreenValueService
                ]
            })
            .overrideTemplate(TMSCustomFieldScreenValueDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldScreenValueDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldScreenValueService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TMSCustomFieldScreenValue(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.tMSCustomFieldScreenValue = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'tMSCustomFieldScreenValueListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TMSCustomFieldScreenValue();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.tMSCustomFieldScreenValue = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'tMSCustomFieldScreenValueListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
