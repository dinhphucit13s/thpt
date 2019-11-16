/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { ProjectBugListDefaultDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default-delete-dialog.component';
import { ProjectBugListDefaultService } from '../../../../../../main/webapp/app/entities/project-bug-list-default/project-bug-list-default.service';

describe('Component Tests', () => {

    describe('ProjectBugListDefault Management Delete Component', () => {
        let comp: ProjectBugListDefaultDeleteDialogComponent;
        let fixture: ComponentFixture<ProjectBugListDefaultDeleteDialogComponent>;
        let service: ProjectBugListDefaultService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectBugListDefaultDeleteDialogComponent],
                providers: [
                    ProjectBugListDefaultService
                ]
            })
            .overrideTemplate(ProjectBugListDefaultDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectBugListDefaultDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectBugListDefaultService);
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
