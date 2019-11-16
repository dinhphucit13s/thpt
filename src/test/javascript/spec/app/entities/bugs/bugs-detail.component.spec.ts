/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { BugsDetailComponent } from '../../../../../../main/webapp/app/entities/bugs/bugs-detail.component';
import { BugsService } from '../../../../../../main/webapp/app/entities/bugs/bugs.service';
import { Bugs } from '../../../../../../main/webapp/app/entities/bugs/bugs.model';

describe('Component Tests', () => {

    describe('Bugs Management Detail Component', () => {
        let comp: BugsDetailComponent;
        let fixture: ComponentFixture<BugsDetailComponent>;
        let service: BugsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BugsDetailComponent],
                providers: [
                    BugsService
                ]
            })
            .overrideTemplate(BugsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BugsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BugsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Bugs(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.bugs).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
