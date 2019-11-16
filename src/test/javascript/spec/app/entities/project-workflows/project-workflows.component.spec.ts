/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { ProjectWorkflowsComponent } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows.component';
import { ProjectWorkflowsService } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows.service';
import { ProjectWorkflows } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows.model';

describe('Component Tests', () => {

    describe('ProjectWorkflows Management Component', () => {
        let comp: ProjectWorkflowsComponent;
        let fixture: ComponentFixture<ProjectWorkflowsComponent>;
        let service: ProjectWorkflowsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectWorkflowsComponent],
                providers: [
                    ProjectWorkflowsService
                ]
            })
            .overrideTemplate(ProjectWorkflowsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectWorkflowsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectWorkflowsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ProjectWorkflows(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.projectWorkflows[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
