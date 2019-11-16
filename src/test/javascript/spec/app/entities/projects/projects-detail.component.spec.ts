/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { ProjectsDetailComponent } from '../../../../../../main/webapp/app/entities/projects/projects-detail.component';
import { ProjectsService } from '../../../../../../main/webapp/app/entities/projects/projects.service';
import { Projects } from '../../../../../../main/webapp/app/entities/projects/projects.model';

describe('Component Tests', () => {

    describe('Projects Management Detail Component', () => {
        let comp: ProjectsDetailComponent;
        let fixture: ComponentFixture<ProjectsDetailComponent>;
        let service: ProjectsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectsDetailComponent],
                providers: [
                    ProjectsService
                ]
            })
            .overrideTemplate(ProjectsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Projects(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.projects).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
