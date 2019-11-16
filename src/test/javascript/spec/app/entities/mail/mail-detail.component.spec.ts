/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { MailDetailComponent } from '../../../../../../main/webapp/app/entities/mail/mail-detail.component';
import { MailService } from '../../../../../../main/webapp/app/entities/mail/mail.service';
import { Mail } from '../../../../../../main/webapp/app/entities/mail/mail.model';

describe('Component Tests', () => {

    describe('Mail Management Detail Component', () => {
        let comp: MailDetailComponent;
        let fixture: ComponentFixture<MailDetailComponent>;
        let service: MailService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [MailDetailComponent],
                providers: [
                    MailService
                ]
            })
            .overrideTemplate(MailDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MailDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MailService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Mail(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.mail).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
