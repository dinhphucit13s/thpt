/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { BusinessLineComponent } from '../../../../../../main/webapp/app/entities/business-line/business-line.component';
import { BusinessLineService } from '../../../../../../main/webapp/app/entities/business-line/business-line.service';
import { BusinessLine } from '../../../../../../main/webapp/app/entities/business-line/business-line.model';

describe('Component Tests', () => {

    describe('BusinessLine Management Component', () => {
        let comp: BusinessLineComponent;
        let fixture: ComponentFixture<BusinessLineComponent>;
        let service: BusinessLineService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BusinessLineComponent],
                providers: [
                    BusinessLineService
                ]
            })
            .overrideTemplate(BusinessLineComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BusinessLineComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BusinessLineService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new BusinessLine(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.businessLines[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
