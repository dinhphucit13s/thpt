/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { IssuesDetailComponent } from '../../../../../../main/webapp/app/entities/issues/issues-detail.component';
import { IssuesService } from '../../../../../../main/webapp/app/entities/issues/issues.service';
import { Issues } from '../../../../../../main/webapp/app/entities/issues/issues.model';

describe('Component Tests', () => {

    describe('Issues Management Detail Component', () => {
        let comp: IssuesDetailComponent;
        let fixture: ComponentFixture<IssuesDetailComponent>;
        let service: IssuesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [IssuesDetailComponent],
                providers: [
                    IssuesService
                ]
            })
            .overrideTemplate(IssuesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(IssuesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(IssuesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Issues(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.issues).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
