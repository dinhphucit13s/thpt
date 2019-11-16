/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { TaskBiddingTrackingTimeDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time-delete-dialog.component';
import { TaskBiddingTrackingTimeService } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time.service';

describe('Component Tests', () => {

    describe('TaskBiddingTrackingTime Management Delete Component', () => {
        let comp: TaskBiddingTrackingTimeDeleteDialogComponent;
        let fixture: ComponentFixture<TaskBiddingTrackingTimeDeleteDialogComponent>;
        let service: TaskBiddingTrackingTimeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TaskBiddingTrackingTimeDeleteDialogComponent],
                providers: [
                    TaskBiddingTrackingTimeService
                ]
            })
            .overrideTemplate(TaskBiddingTrackingTimeDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskBiddingTrackingTimeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskBiddingTrackingTimeService);
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
