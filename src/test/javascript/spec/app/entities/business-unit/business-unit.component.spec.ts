/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { BusinessUnitComponent } from '../../../../../../main/webapp/app/entities/business-unit/business-unit.component';
import { BusinessUnitService } from '../../../../../../main/webapp/app/entities/business-unit/business-unit.service';
import { BusinessUnit } from '../../../../../../main/webapp/app/entities/business-unit/business-unit.model';

describe('Component Tests', () => {

    describe('BusinessUnit Management Component', () => {
        let comp: BusinessUnitComponent;
        let fixture: ComponentFixture<BusinessUnitComponent>;
        let service: BusinessUnitService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BusinessUnitComponent],
                providers: [
                    BusinessUnitService
                ]
            })
            .overrideTemplate(BusinessUnitComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BusinessUnitComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BusinessUnitService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new BusinessUnit(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.businessUnits[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
