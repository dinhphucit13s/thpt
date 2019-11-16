/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { TaskTrackingTimeDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time-delete-dialog.component';
import { TaskTrackingTimeService } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time.service';

describe('Component Tests', () => {

    describe('TaskTrackingTime Management Delete Component', () => {
        let comp: TaskTrackingTimeDeleteDialogComponent;
        let fixture: ComponentFixture<TaskTrackingTimeDeleteDialogComponent>;
        let service: TaskTrackingTimeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TaskTrackingTimeDeleteDialogComponent],
                providers: [
                    TaskTrackingTimeService
                ]
            })
            .overrideTemplate(TaskTrackingTimeDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskTrackingTimeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskTrackingTimeService);
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
