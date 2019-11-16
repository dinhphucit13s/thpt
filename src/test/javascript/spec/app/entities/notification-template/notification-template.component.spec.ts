/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { NotificationTemplateComponent } from '../../../../../../main/webapp/app/entities/notification-template/notification-template.component';
import { NotificationTemplateService } from '../../../../../../main/webapp/app/entities/notification-template/notification-template.service';
import { NotificationTemplate } from '../../../../../../main/webapp/app/entities/notification-template/notification-template.model';

describe('Component Tests', () => {

    describe('NotificationTemplate Management Component', () => {
        let comp: NotificationTemplateComponent;
        let fixture: ComponentFixture<NotificationTemplateComponent>;
        let service: NotificationTemplateService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [NotificationTemplateComponent],
                providers: [
                    NotificationTemplateService
                ]
            })
            .overrideTemplate(NotificationTemplateComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(NotificationTemplateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NotificationTemplateService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new NotificationTemplate(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.notificationTemplates[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
