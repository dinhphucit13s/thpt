/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { AttachmentsComponent } from '../../../../../../main/webapp/app/entities/attachments/attachments.component';
import { AttachmentsService } from '../../../../../../main/webapp/app/entities/attachments/attachments.service';
import { Attachments } from '../../../../../../main/webapp/app/entities/attachments/attachments.model';

describe('Component Tests', () => {

    describe('Attachments Management Component', () => {
        let comp: AttachmentsComponent;
        let fixture: ComponentFixture<AttachmentsComponent>;
        let service: AttachmentsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [AttachmentsComponent],
                providers: [
                    AttachmentsService
                ]
            })
            .overrideTemplate(AttachmentsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AttachmentsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AttachmentsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Attachments(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.attachments[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
