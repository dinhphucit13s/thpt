/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { TaskTrackingTimeDetailComponent } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time-detail.component';
import { TaskTrackingTimeService } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time.service';
import { TaskTrackingTime } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time.model';

describe('Component Tests', () => {

    describe('TaskTrackingTime Management Detail Component', () => {
        let comp: TaskTrackingTimeDetailComponent;
        let fixture: ComponentFixture<TaskTrackingTimeDetailComponent>;
        let service: TaskTrackingTimeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TaskTrackingTimeDetailComponent],
                providers: [
                    TaskTrackingTimeService
                ]
            })
            .overrideTemplate(TaskTrackingTimeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskTrackingTimeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskTrackingTimeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TaskTrackingTime(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.taskTrackingTime).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
