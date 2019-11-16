/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { BusinessUnitManagerComponent } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager.component';
import { BusinessUnitManagerService } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager.service';
import { BusinessUnitManager } from '../../../../../../main/webapp/app/entities/business-unit-manager/business-unit-manager.model';

describe('Component Tests', () => {

    describe('BusinessUnitManager Management Component', () => {
        let comp: BusinessUnitManagerComponent;
        let fixture: ComponentFixture<BusinessUnitManagerComponent>;
        let service: BusinessUnitManagerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BusinessUnitManagerComponent],
                providers: [
                    BusinessUnitManagerService
                ]
            })
            .overrideTemplate(BusinessUnitManagerComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BusinessUnitManagerComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BusinessUnitManagerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new BusinessUnitManager(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.businessUnitManagers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
