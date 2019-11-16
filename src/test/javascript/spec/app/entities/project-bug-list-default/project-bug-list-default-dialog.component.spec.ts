/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { ProjectBugListDefaultDialogComponent } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default-dialog.component';
import { ProjectBugListDefaultService } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default.service';
import { ProjectBugListDefault } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default.model';
import { ProjectsService } from '../../../../../../main/webapp/app/entities/projects';
import { BugListDefaultService } from '../../../../../../main/webapp/app/entities/bug-list-default';

describe('Component Tests', () => {

    describe('ProjectBugListDefault Management Dialog Component', () => {
        let comp: ProjectBugListDefaultDialogComponent;
        let fixture: ComponentFixture<ProjectBugListDefaultDialogComponent>;
        let service: ProjectBugListDefaultService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectBugListDefaultDialogComponent],
                providers: [
                    ProjectsService,
                    BugListDefaultService,
                    ProjectBugListDefaultService
                ]
            })
            .overrideTemplate(ProjectBugListDefaultDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectBugListDefaultDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectBugListDefaultService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ProjectBugListDefault(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.projectBugListDefault = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'projectBugListDefaultListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ProjectBugListDefault();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.projectBugListDefault = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'projectBugListDefaultListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
