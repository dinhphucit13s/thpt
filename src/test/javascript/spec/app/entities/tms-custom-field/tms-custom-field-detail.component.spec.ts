/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldDetailComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field/tms-custom-field-detail.component';
import { TMSCustomFieldService } from '../../../../../../main/webapp/app/entities/tms-custom-field/tms-custom-field.service';
import { TMSCustomField } from '../../../../../../main/webapp/app/entities/tms-custom-field/tms-custom-field.model';

describe('Component Tests', () => {

    describe('TMSCustomField Management Detail Component', () => {
        let comp: TMSCustomFieldDetailComponent;
        let fixture: ComponentFixture<TMSCustomFieldDetailComponent>;
        let service: TMSCustomFieldService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldDetailComponent],
                providers: [
                    TMSCustomFieldService
                ]
            })
            .overrideTemplate(TMSCustomFieldDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TMSCustomField(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.tMSCustomField).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
