/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldScreenDialogComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen-dialog.component';
import { TMSCustomFieldScreenService } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen.service';
import { TMSCustomFieldScreen } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen.model';
import { TMSCustomFieldService } from '../../../../../../main/webapp/app/entities/tms-custom-field';
import { ProjectWorkflowsService } from '../../../../../../main/webapp/app/entities/project-workflows';

describe('Component Tests', () => {

    describe('TMSCustomFieldScreen Management Dialog Component', () => {
        let comp: TMSCustomFieldScreenDialogComponent;
        let fixture: ComponentFixture<TMSCustomFieldScreenDialogComponent>;
        let service: TMSCustomFieldScreenService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldScreenDialogComponent],
                providers: [
                    TMSCustomFieldService,
                    ProjectWorkflowsService,
                    TMSCustomFieldScreenService
                ]
            })
            .overrideTemplate(TMSCustomFieldScreenDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldScreenDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldScreenService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TMSCustomFieldScreen(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.tMSCustomFieldScreen = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'tMSCustomFieldScreenListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TMSCustomFieldScreen();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.tMSCustomFieldScreen = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'tMSCustomFieldScreenListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
