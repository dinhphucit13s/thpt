/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldScreenValueComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value.component';
import { TMSCustomFieldScreenValueService } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value.service';
import { TMSCustomFieldScreenValue } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value.model';

describe('Component Tests', () => {

    describe('TMSCustomFieldScreenValue Management Component', () => {
        let comp: TMSCustomFieldScreenValueComponent;
        let fixture: ComponentFixture<TMSCustomFieldScreenValueComponent>;
        let service: TMSCustomFieldScreenValueService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldScreenValueComponent],
                providers: [
                    TMSCustomFieldScreenValueService
                ]
            })
            .overrideTemplate(TMSCustomFieldScreenValueComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldScreenValueComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldScreenValueService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TMSCustomFieldScreenValue(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.tMSCustomFieldScreenValues[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
