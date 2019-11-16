/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { PackagesDetailComponent } from '../../../../../../main/webapp/app/entities/packages/packages-detail.component';
import { PackagesService } from '../../../../../../main/webapp/app/entities/packages/packages.service';
import { Packages } from '../../../../../../main/webapp/app/entities/packages/packages.model';

describe('Component Tests', () => {

    describe('Packages Management Detail Component', () => {
        let comp: PackagesDetailComponent;
        let fixture: ComponentFixture<PackagesDetailComponent>;
        let service: PackagesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [PackagesDetailComponent],
                providers: [
                    PackagesService
                ]
            })
            .overrideTemplate(PackagesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PackagesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PackagesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Packages(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.packages).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
