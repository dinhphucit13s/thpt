/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { TasksComponent } from '../../../../../../main/webapp/app/entities/tasks/tasks.component';
import { TasksService } from '../../../../../../main/webapp/app/entities/tasks/tasks.service';
import { Tasks } from '../../../../../../main/webapp/app/entities/tasks/tasks.model';

describe('Component Tests', () => {

    describe('Tasks Management Component', () => {
        let comp: TasksComponent;
        let fixture: ComponentFixture<TasksComponent>;
        let service: TasksService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TasksComponent],
                providers: [
                    TasksService
                ]
            })
            .overrideTemplate(TasksComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TasksComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TasksService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Tasks(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.tasks[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
