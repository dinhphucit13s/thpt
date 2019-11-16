/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { MailReceiverComponent } from '../../../../../../main/webapp/app/entities/mail-receiver/mail-receiver.component';
import { MailReceiverService } from '../../../../../../main/webapp/app/entities/mail-receiver/mail-receiver.service';
import { MailReceiver } from '../../../../../../main/webapp/app/entities/mail-receiver/mail-receiver.model';

describe('Component Tests', () => {

    describe('MailReceiver Management Component', () => {
        let comp: MailReceiverComponent;
        let fixture: ComponentFixture<MailReceiverComponent>;
        let service: MailReceiverService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [MailReceiverComponent],
                providers: [
                    MailReceiverService
                ]
            })
            .overrideTemplate(MailReceiverComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MailReceiverComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MailReceiverService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new MailReceiver(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.mailReceivers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
