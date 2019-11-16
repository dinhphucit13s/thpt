/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { LoginTrackingDetailComponent } from '../../../../../../main/webapp/app/entities/login-tracking/login-tracking-detail.component';
import { LoginTrackingService } from '../../../../../../main/webapp/app/entities/login-tracking/login-tracking.service';
import { LoginTracking } from '../../../../../../main/webapp/app/entities/login-tracking/login-tracking.model';

describe('Component Tests', () => {

    describe('LoginTracking Management Detail Component', () => {
        let comp: LoginTrackingDetailComponent;
        let fixture: ComponentFixture<LoginTrackingDetailComponent>;
        let service: LoginTrackingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [LoginTrackingDetailComponent],
                providers: [
                    LoginTrackingService
                ]
            })
            .overrideTemplate(LoginTrackingDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LoginTrackingDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LoginTrackingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new LoginTracking(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.loginTracking).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
