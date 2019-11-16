/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { BugListDefaultDetailComponent } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default-detail.component';
import { BugListDefaultService } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default.service';
import { BugListDefault } from '../../../../../../main/webapp/app/entities/bug-list-default/bug-list-default.model';

describe('Component Tests', () => {

    describe('BugListDefault Management Detail Component', () => {
        let comp: BugListDefaultDetailComponent;
        let fixture: ComponentFixture<BugListDefaultDetailComponent>;
        let service: BugListDefaultService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BugListDefaultDetailComponent],
                providers: [
                    BugListDefaultService
                ]
            })
            .overrideTemplate(BugListDefaultDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BugListDefaultDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BugListDefaultService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new BugListDefault(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.bugListDefault).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
