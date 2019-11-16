/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { ProjectWorkflowsDialogComponent } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows-dialog.component';
import { ProjectWorkflowsService } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows.service';
import { ProjectWorkflows } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows.model';
import { ProjectTemplatesService } from '../../../../../../main/webapp/app/entities/project-templates';

describe('Component Tests', () => {

    describe('ProjectWorkflows Management Dialog Component', () => {
        let comp: ProjectWorkflowsDialogComponent;
        let fixture: ComponentFixture<ProjectWorkflowsDialogComponent>;
        let service: ProjectWorkflowsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectWorkflowsDialogComponent],
                providers: [
                    ProjectTemplatesService,
                    ProjectWorkflowsService
                ]
            })
            .overrideTemplate(ProjectWorkflowsDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectWorkflowsDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectWorkflowsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Array<ProjectWorkflows>();// ProjectWorkflows(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.projectWorkflows = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'projectWorkflowsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Array<ProjectWorkflows>(); // ProjectWorkflows();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.projectWorkflows = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'projectWorkflowsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
