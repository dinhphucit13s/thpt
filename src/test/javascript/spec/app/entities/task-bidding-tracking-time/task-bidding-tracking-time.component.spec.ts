/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { TaskBiddingTrackingTimeComponent } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time.component';
import { TaskBiddingTrackingTimeService } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time.service';
import { TaskBiddingTrackingTime } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time.model';

describe('Component Tests', () => {

    describe('TaskBiddingTrackingTime Management Component', () => {
        let comp: TaskBiddingTrackingTimeComponent;
        let fixture: ComponentFixture<TaskBiddingTrackingTimeComponent>;
        let service: TaskBiddingTrackingTimeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TaskBiddingTrackingTimeComponent],
                providers: [
                    TaskBiddingTrackingTimeService
                ]
            })
            .overrideTemplate(TaskBiddingTrackingTimeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskBiddingTrackingTimeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskBiddingTrackingTimeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TaskBiddingTrackingTime(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.taskBiddingTrackingTimes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
