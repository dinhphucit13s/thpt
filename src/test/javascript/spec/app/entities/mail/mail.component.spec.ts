/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { MailComponent } from '../../../../../../main/webapp/app/entities/mail/mail.component';
import { MailService } from '../../../../../../main/webapp/app/entities/mail/mail.service';
import { Mail } from '../../../../../../main/webapp/app/entities/mail/mail.model';

describe('Component Tests', () => {

    describe('Mail Management Component', () => {
        let comp: MailComponent;
        let fixture: ComponentFixture<MailComponent>;
        let service: MailService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [MailComponent],
                providers: [
                    MailService
                ]
            })
            .overrideTemplate(MailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MailService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Mail(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.mail[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
