/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { BugListDefaultComponent } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default.component';
import { BugListDefaultService } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default.service';
import { BugListDefault } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default.model';

describe('Component Tests', () => {

    describe('BugListDefault Management Component', () => {
        let comp: BugListDefaultComponent;
        let fixture: ComponentFixture<BugListDefaultComponent>;
        let service: BugListDefaultService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BugListDefaultComponent],
                providers: [
                    BugListDefaultService
                ]
            })
            .overrideTemplate(BugListDefaultComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BugListDefaultComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BugListDefaultService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new BugListDefault(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.bugListDefaults[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
