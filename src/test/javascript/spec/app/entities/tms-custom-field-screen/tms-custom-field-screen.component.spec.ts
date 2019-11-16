/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldScreenComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen.component';
import { TMSCustomFieldScreenService } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen.service';
import { TMSCustomFieldScreen } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen.model';

describe('Component Tests', () => {

    describe('TMSCustomFieldScreen Management Component', () => {
        let comp: TMSCustomFieldScreenComponent;
        let fixture: ComponentFixture<TMSCustomFieldScreenComponent>;
        let service: TMSCustomFieldScreenService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldScreenComponent],
                providers: [
                    TMSCustomFieldScreenService
                ]
            })
            .overrideTemplate(TMSCustomFieldScreenComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldScreenComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldScreenService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TMSCustomFieldScreen(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.tMSCustomFieldScreens[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
