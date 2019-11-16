/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldScreenValueDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value-delete-dialog.component';
import { TMSCustomFieldScreenValueService } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value.service';

describe('Component Tests', () => {

    describe('TMSCustomFieldScreenValue Management Delete Component', () => {
        let comp: TMSCustomFieldScreenValueDeleteDialogComponent;
        let fixture: ComponentFixture<TMSCustomFieldScreenValueDeleteDialogComponent>;
        let service: TMSCustomFieldScreenValueService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldScreenValueDeleteDialogComponent],
                providers: [
                    TMSCustomFieldScreenValueService
                ]
            })
            .overrideTemplate(TMSCustomFieldScreenValueDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldScreenValueDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldScreenValueService);
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
