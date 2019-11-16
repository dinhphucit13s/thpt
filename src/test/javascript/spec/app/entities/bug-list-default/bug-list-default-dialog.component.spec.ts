/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { BugListDefaultDialogComponent } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default-dialog.component';
import { BugListDefaultService } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default.service';
import { BugListDefault } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default.model';
import { BusinessLineService } from '../../../../../../main/webapp/app/entities/business-line';

describe('Component Tests', () => {

    describe('BugListDefault Management Dialog Component', () => {
        let comp: BugListDefaultDialogComponent;
        let fixture: ComponentFixture<BugListDefaultDialogComponent>;
        let service: BugListDefaultService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BugListDefaultDialogComponent],
                providers: [
                    BusinessLineService,
                    BugListDefaultService
                ]
            })
            .overrideTemplate(BugListDefaultDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BugListDefaultDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BugListDefaultService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new BugListDefault(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.bugListDefault = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'bugListDefaultListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new BugListDefault();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.bugListDefault = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'bugListDefaultListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
