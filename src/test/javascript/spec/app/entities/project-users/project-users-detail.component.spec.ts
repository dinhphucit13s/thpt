/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { ProjectUsersDetailComponent } from '../../../../../../main/webapp/app/entities/project-users/project-users-detail.component';
import { ProjectUsersService } from '../../../../../../main/webapp/app/entities/project-users/project-users.service';
import { ProjectUsers } from '../../../../../../main/webapp/app/entities/project-users/project-users.model';

describe('Component Tests', () => {

    describe('ProjectUsers Management Detail Component', () => {
        let comp: ProjectUsersDetailComponent;
        let fixture: ComponentFixture<ProjectUsersDetailComponent>;
        let service: ProjectUsersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectUsersDetailComponent],
                providers: [
                    ProjectUsersService
                ]
            })
            .overrideTemplate(ProjectUsersDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectUsersDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectUsersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ProjectUsers(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.projectUsers).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
