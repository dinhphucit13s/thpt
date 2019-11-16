/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { ProjectTemplatesDetailComponent } from '../../../../../../main/webapp/app/entities/project-templates/project-templates-detail.component';
import { ProjectTemplatesService } from '../../../../../../main/webapp/app/entities/project-templates/project-templates.service';
import { ProjectTemplates } from '../../../../../../main/webapp/app/entities/project-templates/project-templates.model';

describe('Component Tests', () => {

    describe('ProjectTemplates Management Detail Component', () => {
        let comp: ProjectTemplatesDetailComponent;
        let fixture: ComponentFixture<ProjectTemplatesDetailComponent>;
        let service: ProjectTemplatesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectTemplatesDetailComponent],
                providers: [
                    ProjectTemplatesService
                ]
            })
            .overrideTemplate(ProjectTemplatesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectTemplatesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectTemplatesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ProjectTemplates(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.projectTemplates).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
