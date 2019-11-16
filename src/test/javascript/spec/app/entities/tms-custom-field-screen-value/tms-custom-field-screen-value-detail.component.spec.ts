/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldScreenValueDetailComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value-detail.component';
import { TMSCustomFieldScreenValueService } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value.service';
import { TMSCustomFieldScreenValue } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen-value/tms-custom-field-screen-value.model';

describe('Component Tests', () => {

    describe('TMSCustomFieldScreenValue Management Detail Component', () => {
        let comp: TMSCustomFieldScreenValueDetailComponent;
        let fixture: ComponentFixture<TMSCustomFieldScreenValueDetailComponent>;
        let service: TMSCustomFieldScreenValueService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldScreenValueDetailComponent],
                providers: [
                    TMSCustomFieldScreenValueService
                ]
            })
            .overrideTemplate(TMSCustomFieldScreenValueDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldScreenValueDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldScreenValueService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TMSCustomFieldScreenValue(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.tMSCustomFieldScreenValue).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
