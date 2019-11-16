/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { TMSLogHistoryDetailComponent } from '../../../../../../main/webapp/app/entities/tms-log-history/tms-log-history-detail.component';
import { TMSLogHistoryService } from '../../../../../../main/webapp/app/entities/tms-log-history/tms-log-history.service';
import { TMSLogHistory } from '../../../../../../main/webapp/app/entities/tms-log-history/tms-log-history.model';

describe('Component Tests', () => {

    describe('TMSLogHistory Management Detail Component', () => {
        let comp: TMSLogHistoryDetailComponent;
        let fixture: ComponentFixture<TMSLogHistoryDetailComponent>;
        let service: TMSLogHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSLogHistoryDetailComponent],
                providers: [
                    TMSLogHistoryService
                ]
            })
            .overrideTemplate(TMSLogHistoryDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSLogHistoryDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSLogHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TMSLogHistory(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.tMSLogHistory).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
