/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { NotificationTemplateDetailComponent } from '../../../../../../main/webapp/app/entities/notification-template/notification-template-detail.component';
import { NotificationTemplateService } from '../../../../../../main/webapp/app/entities/notification-template/notification-template.service';
import { NotificationTemplate } from '../../../../../../main/webapp/app/entities/notification-template/notification-template.model';

describe('Component Tests', () => {

    describe('NotificationTemplate Management Detail Component', () => {
        let comp: NotificationTemplateDetailComponent;
        let fixture: ComponentFixture<NotificationTemplateDetailComponent>;
        let service: NotificationTemplateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [NotificationTemplateDetailComponent],
                providers: [
                    NotificationTemplateService
                ]
            })
            .overrideTemplate(NotificationTemplateDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(NotificationTemplateDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationTemplateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new NotificationTemplate(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.notificationTemplate).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
