/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { TaskBiddingTrackingTimeDetailComponent } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time-detail.component';
import { TaskBiddingTrackingTimeService } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time.service';
import { TaskBiddingTrackingTime } from '../../../../../../main/webapp/app/entities/task-bidding-tracking-time/task-bidding-tracking-time.model';

describe('Component Tests', () => {

    describe('TaskBiddingTrackingTime Management Detail Component', () => {
        let comp: TaskBiddingTrackingTimeDetailComponent;
        let fixture: ComponentFixture<TaskBiddingTrackingTimeDetailComponent>;
        let service: TaskBiddingTrackingTimeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TaskBiddingTrackingTimeDetailComponent],
                providers: [
                    TaskBiddingTrackingTimeService
                ]
            })
            .overrideTemplate(TaskBiddingTrackingTimeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskBiddingTrackingTimeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskBiddingTrackingTimeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TaskBiddingTrackingTime(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.taskBiddingTrackingTime).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
