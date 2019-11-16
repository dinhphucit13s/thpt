/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { BusinessUnitDetailComponent } from '../../../../../../main/webapp/app/entities/business-unit/business-unit-detail.component';
import { BusinessUnitService } from '../../../../../../main/webapp/app/entities/business-unit/business-unit.service';
import { BusinessUnit } from '../../../../../../main/webapp/app/entities/business-unit/business-unit.model';

describe('Component Tests', () => {

    describe('BusinessUnit Management Detail Component', () => {
        let comp: BusinessUnitDetailComponent;
        let fixture: ComponentFixture<BusinessUnitDetailComponent>;
        let service: BusinessUnitService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BusinessUnitDetailComponent],
                providers: [
                    BusinessUnitService
                ]
            })
            .overrideTemplate(BusinessUnitDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BusinessUnitDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BusinessUnitService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new BusinessUnit(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.businessUnit).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
