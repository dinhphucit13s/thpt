/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { ProjectTemplatesComponent } from '../../../../../../main/webapp/app/entities/project-templates/project-templates.component';
import { ProjectTemplatesService } from '../../../../../../main/webapp/app/entities/project-templates/project-templates.service';
import { ProjectTemplates } from '../../../../../../main/webapp/app/entities/project-templates/project-templates.model';

describe('Component Tests', () => {

    describe('ProjectTemplates Management Component', () => {
        let comp: ProjectTemplatesComponent;
        let fixture: ComponentFixture<ProjectTemplatesComponent>;
        let service: ProjectTemplatesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectTemplatesComponent],
                providers: [
                    ProjectTemplatesService
                ]
            })
            .overrideTemplate(ProjectTemplatesComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectTemplatesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectTemplatesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ProjectTemplates(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.projectTemplates[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
