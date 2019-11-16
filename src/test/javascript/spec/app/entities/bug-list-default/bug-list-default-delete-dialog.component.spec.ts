/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { BugListDefaultDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default-delete-dialog.component';
import { BugListDefaultService } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default.service';

describe('Component Tests', () => {

    describe('BugListDefault Management Delete Component', () => {
        let comp: BugListDefaultDeleteDialogComponent;
        let fixture: ComponentFixture<BugListDefaultDeleteDialogComponent>;
        let service: BugListDefaultService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BugListDefaultDeleteDialogComponent],
                providers: [
                    BugListDefaultService
                ]
            })
            .overrideTemplate(BugListDefaultDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BugListDefaultDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BugListDefaultService);
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
