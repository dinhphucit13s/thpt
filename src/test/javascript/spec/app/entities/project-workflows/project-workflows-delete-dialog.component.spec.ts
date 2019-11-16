/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { ProjectWorkflowsDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows-delete-dialog.component';
import { ProjectWorkflowsService } from '../../../../../../main/webapp/app/entities/project-workflows/project-workflows.service';

describe('Component Tests', () => {

    describe('ProjectWorkflows Management Delete Component', () => {
        let comp: ProjectWorkflowsDeleteDialogComponent;
        let fixture: ComponentFixture<ProjectWorkflowsDeleteDialogComponent>;
        let service: ProjectWorkflowsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectWorkflowsDeleteDialogComponent],
                providers: [
                    ProjectWorkflowsService
                ]
            })
            .overrideTemplate(ProjectWorkflowsDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectWorkflowsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectWorkflowsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
