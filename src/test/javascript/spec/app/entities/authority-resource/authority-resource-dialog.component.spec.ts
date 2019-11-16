/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DtmsTestModule } from '../../../test.module';
import { AuthorityResourceDialogComponent } from '../../../../../../main/webapp/app/entities/authority-resource/authority-resource-dialog.component';
import { AuthorityResourceService } from '../../../../../../main/webapp/app/entities/authority-resource/authority-resource.service';
import { AuthorityResource } from '../../../../../../main/webapp/app/entities/authority-resource/authority-resource.model';

describe('Component Tests', () => {

    describe('AuthorityResource Management Dialog Component', () => {
        let comp: AuthorityResourceDialogComponent;
        let fixture: ComponentFixture<AuthorityResourceDialogComponent>;
        let service: AuthorityResourceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [AuthorityResourceDialogComponent],
                providers: [
                    AuthorityResourceService
                ]
            })
            .overrideTemplate(AuthorityResourceDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AuthorityResourceDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuthorityResourceService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new AuthorityResource(123);
                        const authorityResource = new Array();
                        authorityResource.push(entity);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.authorityResource = authorityResource;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'authorityResourceListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new AuthorityResource();
                        const authorityResource = new Array();
                        authorityResource.push(entity);
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.authorityResource = authorityResource;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'authorityResourceListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
