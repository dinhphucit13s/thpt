/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { TMSLogHistoryComponent } from '../../../../../../main/webapp/app/entities/tms-log-history/tms-log-history.component';
import { TMSLogHistoryService } from '../../../../../../main/webapp/app/entities/tms-log-history/tms-log-history.service';
import { TMSLogHistory } from '../../../../../../main/webapp/app/entities/tms-log-history/tms-log-history.model';

describe('Component Tests', () => {

    describe('TMSLogHistory Management Component', () => {
        let comp: TMSLogHistoryComponent;
        let fixture: ComponentFixture<TMSLogHistoryComponent>;
        let service: TMSLogHistoryService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSLogHistoryComponent],
                providers: [
                    TMSLogHistoryService
                ]
            })
            .overrideTemplate(TMSLogHistoryComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSLogHistoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSLogHistoryService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TMSLogHistory(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.tMSLogHistories[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
