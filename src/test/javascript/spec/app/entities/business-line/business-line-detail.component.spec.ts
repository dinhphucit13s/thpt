/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { BusinessLineDetailComponent } from '../../../../../../main/webapp/app/entities/business-line/business-line-detail.component';
import { BusinessLineService } from '../../../../../../main/webapp/app/entities/business-line/business-line.service';
import { BusinessLine } from '../../../../../../main/webapp/app/entities/business-line/business-line.model';

describe('Component Tests', () => {

    describe('BusinessLine Management Detail Component', () => {
        let comp: BusinessLineDetailComponent;
        let fixture: ComponentFixture<BusinessLineDetailComponent>;
        let service: BusinessLineService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BusinessLineDetailComponent],
                providers: [
                    BusinessLineService
                ]
            })
            .overrideTemplate(BusinessLineDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BusinessLineDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BusinessLineService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new BusinessLine(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.businessLine).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
