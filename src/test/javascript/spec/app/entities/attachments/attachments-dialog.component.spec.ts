/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { AttachmentsDialogComponent } from '../../../../../../main/webapp/app/entities/attachments/attachments-dialog.component';
import { AttachmentsService } from '../../../../../../main/webapp/app/entities/attachments/attachments.service';
import { Attachments } from '../../../../../../main/webapp/app/entities/attachments/attachments.model';
import { BugsService } from '../../../../../../main/webapp/app/entities/bugs';
import { NotesService } from '../../../../../../main/webapp/app/entities/notes';
import { IssuesService } from '../../../../../../main/webapp/app/entities/issues';
import { MailService } from '../../../../../../main/webapp/app/entities/mail';

describe('Component Tests', () => {

    describe('Attachments Management Dialog Component', () => {
        let comp: AttachmentsDialogComponent;
        let fixture: ComponentFixture<AttachmentsDialogComponent>;
        let service: AttachmentsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [AttachmentsDialogComponent],
                providers: [
                    BugsService,
                    NotesService,
                    IssuesService,
                    MailService,
                    AttachmentsService
                ]
            })
            .overrideTemplate(AttachmentsDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AttachmentsDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttachmentsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Attachments(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.attachments = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'attachmentsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Attachments();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.attachments = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'attachmentsListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
