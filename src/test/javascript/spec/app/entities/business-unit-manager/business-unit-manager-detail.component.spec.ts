/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { BusinessUnitManagerDetailComponent } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager-detail.component';
import { BusinessUnitManagerService } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager.service';
import { BusinessUnitManager } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager.model';

describe('Component Tests', () => {

    describe('BusinessUnitManager Management Detail Component', () => {
        let comp: BusinessUnitManagerDetailComponent;
        let fixture: ComponentFixture<BusinessUnitManagerDetailComponent>;
        let service: BusinessUnitManagerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BusinessUnitManagerDetailComponent],
                providers: [
                    BusinessUnitManagerService
                ]
            })
            .overrideTemplate(BusinessUnitManagerDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BusinessUnitManagerDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BusinessUnitManagerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new BusinessUnitManager(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.businessUnitManager).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
