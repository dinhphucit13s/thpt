/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { MailReceiverDetailComponent } from '../../../../../../main/webapp/app/entities/mail-receiver/mail-receiver-detail.component';
import { MailReceiverService } from '../../../../../../main/webapp/app/entities/mail-receiver/mail-receiver.service';
import { MailReceiver } from '../../../../../../main/webapp/app/entities/mail-receiver/mail-receiver.model';

describe('Component Tests', () => {

    describe('MailReceiver Management Detail Component', () => {
        let comp: MailReceiverDetailComponent;
        let fixture: ComponentFixture<MailReceiverDetailComponent>;
        let service: MailReceiverService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [MailReceiverDetailComponent],
                providers: [
                    MailReceiverService
                ]
            })
            .overrideTemplate(MailReceiverDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MailReceiverDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MailReceiverService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new MailReceiver(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.mailReceiver).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
