/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field/tms-custom-field.component';
import { TMSCustomFieldService } from '../../../../../../main/webapp/app/entities/tms-custom-field/tms-custom-field.service';
import { TMSCustomField } from '../../../../../../main/webapp/app/entities/tms-custom-field/tms-custom-field.model';

describe('Component Tests', () => {

    describe('TMSCustomField Management Component', () => {
        let comp: TMSCustomFieldComponent;
        let fixture: ComponentFixture<TMSCustomFieldComponent>;
        let service: TMSCustomFieldService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldComponent],
                providers: [
                    TMSCustomFieldService
                ]
            })
            .overrideTemplate(TMSCustomFieldComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TMSCustomField(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.tMSCustomFields[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
