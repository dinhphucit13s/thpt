/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { ProjectBugListDefaultComponent } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default.component';
import { ProjectBugListDefaultService } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default.service';
import { ProjectBugListDefault } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default.model';

describe('Component Tests', () => {

    describe('ProjectBugListDefault Management Component', () => {
        let comp: ProjectBugListDefaultComponent;
        let fixture: ComponentFixture<ProjectBugListDefaultComponent>;
        let service: ProjectBugListDefaultService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectBugListDefaultComponent],
                providers: [
                    ProjectBugListDefaultService
                ]
            })
            .overrideTemplate(ProjectBugListDefaultComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectBugListDefaultComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectBugListDefaultService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ProjectBugListDefault(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.projectBugListDefaults[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
