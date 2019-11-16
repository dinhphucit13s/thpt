/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { DtmsMonitoringDetailComponent } from '../../../../../../main/webapp/app/entities/dtms-monitoring/dtms-monitoring-detail.component';
import { DtmsMonitoringService } from '../../../../../../main/webapp/app/entities/dtms-monitoring/dtms-monitoring.service';
import { DtmsMonitoring } from '../../../../../../main/webapp/app/entities/dtms-monitoring/dtms-monitoring.model';

describe('Component Tests', () => {

    describe('DtmsMonitoring Management Detail Component', () => {
        let comp: DtmsMonitoringDetailComponent;
        let fixture: ComponentFixture<DtmsMonitoringDetailComponent>;
        let service: DtmsMonitoringService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [DtmsMonitoringDetailComponent],
                providers: [
                    DtmsMonitoringService
                ]
            })
            .overrideTemplate(DtmsMonitoringDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DtmsMonitoringDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DtmsMonitoringService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new DtmsMonitoring(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.dtmsMonitoring).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
