/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { TasksDetailComponent } from '../../../../../../main/webapp/app/entities/tasks/tasks-detail.component';
import { TasksService } from '../../../../../../main/webapp/app/entities/tasks/tasks.service';
import { Tasks } from '../../../../../../main/webapp/app/entities/tasks/tasks.model';

describe('Component Tests', () => {

    describe('Tasks Management Detail Component', () => {
        let comp: TasksDetailComponent;
        let fixture: ComponentFixture<TasksDetailComponent>;
        let service: TasksService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TasksDetailComponent],
                providers: [
                    TasksService
                ]
            })
            .overrideTemplate(TasksDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TasksDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TasksService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Tasks(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.tasks).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
