/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { IssuesComponent } from '../../../../../../main/webapp/app/entities/issues/issues.component';
import { IssuesService } from '../../../../../../main/webapp/app/entities/issues/issues.service';
import { Issues } from '../../../../../../main/webapp/app/entities/issues/issues.model';

describe('Component Tests', () => {

    describe('Issues Management Component', () => {
        let comp: IssuesComponent;
        let fixture: ComponentFixture<IssuesComponent>;
        let service: IssuesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [IssuesComponent],
                providers: [
                    IssuesService
                ]
            })
            .overrideTemplate(IssuesComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IssuesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IssuesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Issues(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.issues[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
