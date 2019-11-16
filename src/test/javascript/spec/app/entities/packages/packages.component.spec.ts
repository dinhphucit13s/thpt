/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { PackagesComponent } from '../../../../../../main/webapp/app/entities/packages/packages.component';
import { PackagesService } from '../../../../../../main/webapp/app/entities/packages/packages.service';
import { Packages } from '../../../../../../main/webapp/app/entities/packages/packages.model';

describe('Component Tests', () => {

    describe('Packages Management Component', () => {
        let comp: PackagesComponent;
        let fixture: ComponentFixture<PackagesComponent>;
        let service: PackagesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [PackagesComponent],
                providers: [
                    PackagesService
                ]
            })
            .overrideTemplate(PackagesComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PackagesComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PackagesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Packages(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.packages[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
