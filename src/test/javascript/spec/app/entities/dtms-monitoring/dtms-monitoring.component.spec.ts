/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { DtmsMonitoringComponent } from '../../../../../../main/webapp/app/entities/dtms-monitoring/dtms-monitoring.component';
import { DtmsMonitoringService } from '../../../../../../main/webapp/app/entities/dtms-monitoring/dtms-monitoring.service';
import { DtmsMonitoring } from '../../../../../../main/webapp/app/entities/dtms-monitoring/dtms-monitoring.model';

describe('Component Tests', () => {

    describe('DtmsMonitoring Management Component', () => {
        let comp: DtmsMonitoringComponent;
        let fixture: ComponentFixture<DtmsMonitoringComponent>;
        let service: DtmsMonitoringService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [DtmsMonitoringComponent],
                providers: [
                    DtmsMonitoringService
                ]
            })
            .overrideTemplate(DtmsMonitoringComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DtmsMonitoringComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DtmsMonitoringService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new DtmsMonitoring(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.dtmsMonitorings[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
