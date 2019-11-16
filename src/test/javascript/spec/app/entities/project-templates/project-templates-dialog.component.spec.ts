/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { ProjectTemplatesDialogComponent } from '../../../../../../main/webapp/app/entities/project-templates/project-templates-dialog.component';
import { ProjectTemplatesService } from '../../../../../../main/webapp/app/entities/project-templates/project-templates.service';
import { ProjectTemplates } from '../../../../../../main/webapp/app/entities/project-templates/project-templates.model';
import { BusinessLineService } from '../../../../../../main/webapp/app/entities/business-line';

describe('Component Tests', () => {

    describe('ProjectTemplates Management Dialog Component', () => {
        let comp: ProjectTemplatesDialogComponent;
        let fixture: ComponentFixture<ProjectTemplatesDialogComponent>;
        let service: ProjectTemplatesService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [ProjectTemplatesDialogComponent],
                providers: [
                    BusinessLineService,
                    ProjectTemplatesService
                ]
            })
            .overrideTemplate(ProjectTemplatesDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ProjectTemplatesDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProjectTemplatesService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ProjectTemplates(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.projectTemplates = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'projectTemplatesListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ProjectTemplates();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.projectTemplates = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'projectTemplatesListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
