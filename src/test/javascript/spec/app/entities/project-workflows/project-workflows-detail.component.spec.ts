/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { ProjectWorkflowsDetailComponent } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows-detail.component';
import { ProjectWorkflowsService } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows.service';
import { ProjectWorkflows } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows.model';

describe('Component Tests', () => {

    describe('ProjectWorkflows Management Detail Component', () => {
        let comp: ProjectWorkflowsDetailComponent;
        let fixture: ComponentFixture<ProjectWorkflowsDetailComponent>;
        let service: ProjectWorkflowsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectWorkflowsDetailComponent],
                providers: [
                    ProjectWorkflowsService
                ]
            })
            .overrideTemplate(ProjectWorkflowsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectWorkflowsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectWorkflowsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ProjectWorkflows(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.projectWorkflows).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
