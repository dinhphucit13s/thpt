/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { BusinessUnitManagerDialogComponent } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager-dialog.component';
import { BusinessUnitManagerService } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager.service';
import { BusinessUnitManager } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager.model';
import { BusinessUnitService } from '../../../../../../main/webapp/app/entities/business-unit';
import { UserService } from '../../../../../../main/webapp/app/shared';

describe('Component Tests', () => {

    describe('BusinessUnitManager Management Dialog Component', () => {
        let comp: BusinessUnitManagerDialogComponent;
        let fixture: ComponentFixture<BusinessUnitManagerDialogComponent>;
        let service: BusinessUnitManagerService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BusinessUnitManagerDialogComponent],
                providers: [
                    BusinessUnitService,
                    UserService,
                    BusinessUnitManagerService
                ]
            })
            .overrideTemplate(BusinessUnitManagerDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BusinessUnitManagerDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BusinessUnitManagerService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new BusinessUnitManager(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.businessUnitManager = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'businessUnitManagerListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new BusinessUnitManager();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.businessUnitManager = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'businessUnitManagerListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
