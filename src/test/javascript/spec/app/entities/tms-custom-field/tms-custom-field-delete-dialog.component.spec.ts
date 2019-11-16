/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field/tms-custom-field-delete-dialog.component';
import { TMSCustomFieldService } from '../../../../../../main/webapp/app/entities/tms-custom-field/tms-custom-field.service';

describe('Component Tests', () => {

    describe('TMSCustomField Management Delete Component', () => {
        let comp: TMSCustomFieldDeleteDialogComponent;
        let fixture: ComponentFixture<TMSCustomFieldDeleteDialogComponent>;
        let service: TMSCustomFieldService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldDeleteDialogComponent],
                providers: [
                    TMSCustomFieldService
                ]
            })
            .overrideTemplate(TMSCustomFieldDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldService);
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
