/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { PurchaseOrdersDialogComponent } from '../../../../../../main/webapp/app/entities/purchase-orders/purchase-orders-dialog.component';
import { PurchaseOrdersService } from '../../../../../../main/webapp/app/entities/purchase-orders/purchase-orders.service';
import { PurchaseOrders } from '../../../../../../main/webapp/app/entities/purchase-orders/purchase-orders.model';
import { ProjectsService } from '../../../../../../main/webapp/app/entities/projects';
import { ProjectUsersService } from '../../../../../../main/webapp/app/entities/project-users';

describe('Component Tests', () => {

    describe('PurchaseOrders Management Dialog Component', () => {
        let comp: PurchaseOrdersDialogComponent;
        let fixture: ComponentFixture<PurchaseOrdersDialogComponent>;
        let service: PurchaseOrdersService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [PurchaseOrdersDialogComponent],
                providers: [
                    ProjectsService,
                    ProjectUsersService,
                    PurchaseOrdersService
                ]
            })
            .overrideTemplate(PurchaseOrdersDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PurchaseOrdersDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseOrdersService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new PurchaseOrders(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.purchaseOrders = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'purchaseOrdersListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new PurchaseOrders();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.purchaseOrders = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'purchaseOrdersListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
