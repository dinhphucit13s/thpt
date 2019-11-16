/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { BugsComponent } from '../../../../../../main/webapp/app/entities/bugs/bugs.component';
import { BugsService } from '../../../../../../main/webapp/app/entities/bugs/bugs.service';
import { Bugs } from '../../../../../../main/webapp/app/entities/bugs/bugs.model';

describe('Component Tests', () => {

    describe('Bugs Management Component', () => {
        let comp: BugsComponent;
        let fixture: ComponentFixture<BugsComponent>;
        let service: BugsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [BugsComponent],
                providers: [
                    BugsService
                ]
            })
            .overrideTemplate(BugsComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BugsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BugsService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Bugs(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.bugs[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
