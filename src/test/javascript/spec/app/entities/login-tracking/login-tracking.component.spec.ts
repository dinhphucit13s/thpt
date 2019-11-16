/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { LoginTrackingComponent } from '../../../../../../main/webapp/app/entities/login-tracking/login-tracking.component';
import { LoginTrackingService } from '../../../../../../main/webapp/app/entities/login-tracking/login-tracking.service';
import { LoginTracking } from '../../../../../../main/webapp/app/entities/login-tracking/login-tracking.model';

describe('Component Tests', () => {

    describe('LoginTracking Management Component', () => {
        let comp: LoginTrackingComponent;
        let fixture: ComponentFixture<LoginTrackingComponent>;
        let service: LoginTrackingService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [LoginTrackingComponent],
                providers: [
                    LoginTrackingService
                ]
            })
            .overrideTemplate(LoginTrackingComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LoginTrackingComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LoginTrackingService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new LoginTracking(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.loginTrackings[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
