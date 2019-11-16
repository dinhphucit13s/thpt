/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { BusinessUnitManagerDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager-delete-dialog.component';
import { BusinessUnitManagerService } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager.service';

describe('Component Tests', () => {

    describe('BusinessUnitManager Management Delete Component', () => {
        let comp: BusinessUnitManagerDeleteDialogComponent;
        let fixture: ComponentFixture<BusinessUnitManagerDeleteDialogComponent>;
        let service: BusinessUnitManagerService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BusinessUnitManagerDeleteDialogComponent],
                providers: [
                    BusinessUnitManagerService
                ]
            })
            .overrideTemplate(BusinessUnitManagerDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BusinessUnitManagerDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BusinessUnitManagerService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
