/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldScreenDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen-delete-dialog.component';
import { TMSCustomFieldScreenService } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen.service';

describe('Component Tests', () => {

    describe('TMSCustomFieldScreen Management Delete Component', () => {
        let comp: TMSCustomFieldScreenDeleteDialogComponent;
        let fixture: ComponentFixture<TMSCustomFieldScreenDeleteDialogComponent>;
        let service: TMSCustomFieldScreenService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldScreenDeleteDialogComponent],
                providers: [
                    TMSCustomFieldScreenService
                ]
            })
            .overrideTemplate(TMSCustomFieldScreenDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldScreenDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldScreenService);
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
