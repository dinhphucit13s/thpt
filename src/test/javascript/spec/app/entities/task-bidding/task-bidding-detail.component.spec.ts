/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { TaskBiddingDetailComponent } from '../../../../../../main/webapp/app/entities/task-bidding/task-bidding-detail.component';
import { TaskBiddingService } from '../../../../../../main/webapp/app/entities/task-bidding/task-bidding.service';
import { TaskBidding } from '../../../../../../main/webapp/app/entities/task-bidding/task-bidding.model';

describe('Component Tests', () => {

    describe('TaskBidding Management Detail Component', () => {
        let comp: TaskBiddingDetailComponent;
        let fixture: ComponentFixture<TaskBiddingDetailComponent>;
        let service: TaskBiddingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TaskBiddingDetailComponent],
                providers: [
                    TaskBiddingService
                ]
            })
            .overrideTemplate(TaskBiddingDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskBiddingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskBiddingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TaskBidding(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.taskBidding).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
