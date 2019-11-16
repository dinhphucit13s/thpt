/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { TMSLogHistoryDialogComponent } from '../../../../../../main/webapp/app/entities/tms-log-history/tms-log-history-dialog.component';
import { TMSLogHistoryService } from '../../../../../../main/webapp/app/entities/tms-log-history/tms-log-history.service';
import { TMSLogHistory } from '../../../../../../main/webapp/app/entities/tms-log-history/tms-log-history.model';
import { ProjectsService } from '../../../../../../main/webapp/app/entities/projects';
import { PurchaseOrdersService } from '../../../../../../main/webapp/app/entities/purchase-orders';
import { PackagesService } from '../../../../../../main/webapp/app/entities/packages';
import { TasksService } from '../../../../../../main/webapp/app/entities/tasks';

describe('Component Tests', () => {

    describe('TMSLogHistory Management Dialog Component', () => {
        let comp: TMSLogHistoryDialogComponent;
        let fixture: ComponentFixture<TMSLogHistoryDialogComponent>;
        let service: TMSLogHistoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSLogHistoryDialogComponent],
                providers: [
                    ProjectsService,
                    PurchaseOrdersService,
                    PackagesService,
                    TasksService,
                    TMSLogHistoryService
                ]
            })
            .overrideTemplate(TMSLogHistoryDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSLogHistoryDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSLogHistoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TMSLogHistory(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.tMSLogHistory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'tMSLogHistoryListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TMSLogHistory();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.tMSLogHistory = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'tMSLogHistoryListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
