/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { ProjectsComponent } from '../../../../../../main/webapp/app/entities/projects/projects.component';
import { ProjectsService } from '../../../../../../main/webapp/app/entities/projects/projects.service';
import { Projects } from '../../../../../../main/webapp/app/entities/projects/projects.model';

describe('Component Tests', () => {

    describe('Projects Management Component', () => {
        let comp: ProjectsComponent;
        let fixture: ComponentFixture<ProjectsComponent>;
        let service: ProjectsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectsComponent],
                providers: [
                    ProjectsService
                ]
            })
            .overrideTemplate(ProjectsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Projects(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.projects[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
