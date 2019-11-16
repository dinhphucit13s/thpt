/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { ProjectUsersComponent } from '../../../../../../main/webapp/app/entities/project-users/project-users.component';
import { ProjectUsersService } from '../../../../../../main/webapp/app/entities/project-users/project-users.service';
import { ProjectUsers } from '../../../../../../main/webapp/app/entities/project-users/project-users.model';

describe('Component Tests', () => {

    describe('ProjectUsers Management Component', () => {
        let comp: ProjectUsersComponent;
        let fixture: ComponentFixture<ProjectUsersComponent>;
        let service: ProjectUsersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectUsersComponent],
                providers: [
                    ProjectUsersService
                ]
            })
            .overrideTemplate(ProjectUsersComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectUsersComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectUsersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ProjectUsers(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.projectUsers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
