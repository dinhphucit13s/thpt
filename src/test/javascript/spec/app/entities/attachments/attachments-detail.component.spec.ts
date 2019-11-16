/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { AttachmentsDetailComponent } from '../../../../../../main/webapp/app/entities/attachments/attachments-detail.component';
import { AttachmentsService } from '../../../../../../main/webapp/app/entities/attachments/attachments.service';
import { Attachments } from '../../../../../../main/webapp/app/entities/attachments/attachments.model';

describe('Component Tests', () => {

    describe('Attachments Management Detail Component', () => {
        let comp: AttachmentsDetailComponent;
        let fixture: ComponentFixture<AttachmentsDetailComponent>;
        let service: AttachmentsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [AttachmentsDetailComponent],
                providers: [
                    AttachmentsService
                ]
            })
            .overrideTemplate(AttachmentsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AttachmentsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttachmentsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Attachments(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.attachments).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
