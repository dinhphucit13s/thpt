/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { TaskBiddingTrackingTimeDialogComponent } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time-dialog.component';
import { TaskBiddingTrackingTimeService } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time.service';
import { TaskBiddingTrackingTime } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time.model';
import { TasksService } from '../../../../../../main/webapp/app/entities/tasks';

describe('Component Tests', () => {

    describe('TaskBiddingTrackingTime Management Dialog Component', () => {
        let comp: TaskBiddingTrackingTimeDialogComponent;
        let fixture: ComponentFixture<TaskBiddingTrackingTimeDialogComponent>;
        let service: TaskBiddingTrackingTimeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TaskBiddingTrackingTimeDialogComponent],
                providers: [
                    TasksService,
                    TaskBiddingTrackingTimeService
                ]
            })
            .overrideTemplate(TaskBiddingTrackingTimeDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskBiddingTrackingTimeDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskBiddingTrackingTimeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TaskBiddingTrackingTime(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.taskBiddingTrackingTime = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'taskBiddingTrackingTimeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TaskBiddingTrackingTime();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.taskBiddingTrackingTime = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'taskBiddingTrackingTimeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
