/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { ProjectBugListDefaultDetailComponent } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default-detail.component';
import { ProjectBugListDefaultService } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default.service';
import { ProjectBugListDefault } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default.model';

describe('Component Tests', () => {

    describe('ProjectBugListDefault Management Detail Component', () => {
        let comp: ProjectBugListDefaultDetailComponent;
        let fixture: ComponentFixture<ProjectBugListDefaultDetailComponent>;
        let service: ProjectBugListDefaultService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectBugListDefaultDetailComponent],
                providers: [
                    ProjectBugListDefaultService
                ]
            })
            .overrideTemplate(ProjectBugListDefaultDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectBugListDefaultDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectBugListDefaultService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ProjectBugListDefault(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.projectBugListDefault).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
