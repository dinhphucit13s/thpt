/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { TaskBiddingComponent } from '../../../../../../main/webapp/app/entities/task-bidding/task-bidding.component';
import { TaskBiddingService } from '../../../../../../main/webapp/app/entities/task-bidding/task-bidding.service';
import { TaskBidding } from '../../../../../../main/webapp/app/entities/task-bidding/task-bidding.model';

describe('Component Tests', () => {

    describe('TaskBidding Management Component', () => {
        let comp: TaskBiddingComponent;
        let fixture: ComponentFixture<TaskBiddingComponent>;
        let service: TaskBiddingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TaskBiddingComponent],
                providers: [
                    TaskBiddingService
                ]
            })
            .overrideTemplate(TaskBiddingComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TaskBiddingComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TaskBiddingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TaskBidding(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.taskBiddings[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
