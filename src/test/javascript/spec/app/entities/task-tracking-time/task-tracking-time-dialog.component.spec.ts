/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { TaskTrackingTimeDialogComponent } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time-dialog.component';
import { TaskTrackingTimeService } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time.service';
import { TaskTrackingTime } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time.model';

describe('Component Tests', () => {

    describe('TaskTrackingTime Management Dialog Component', () => {
        let comp: TaskTrackingTimeDialogComponent;
        let fixture: ComponentFixture<TaskTrackingTimeDialogComponent>;
        let service: TaskTrackingTimeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TaskTrackingTimeDialogComponent],
                providers: [
                    TaskTrackingTimeService
                ]
            })
            .overrideTemplate(TaskTrackingTimeDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskTrackingTimeDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskTrackingTimeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TaskTrackingTime(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.taskTrackingTime = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'taskTrackingTimeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TaskTrackingTime();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.taskTrackingTime = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'taskTrackingTimeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
