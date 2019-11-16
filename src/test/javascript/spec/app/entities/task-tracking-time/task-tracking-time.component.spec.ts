/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { TaskTrackingTimeComponent } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time.component';
import { TaskTrackingTimeService } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time.service';
import { TaskTrackingTime } from '../../../../../../main/webapp/app/entities/task-tracking-time/task-tracking-time.model';

describe('Component Tests', () => {

    describe('TaskTrackingTime Management Component', () => {
        let comp: TaskTrackingTimeComponent;
        let fixture: ComponentFixture<TaskTrackingTimeComponent>;
        let service: TaskTrackingTimeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TaskTrackingTimeComponent],
                providers: [
                    TaskTrackingTimeService
                ]
            })
            .overrideTemplate(TaskTrackingTimeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskTrackingTimeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskTrackingTimeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TaskTrackingTime(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.taskTrackingTimes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
