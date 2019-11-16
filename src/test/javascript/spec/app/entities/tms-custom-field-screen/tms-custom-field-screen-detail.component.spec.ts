/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { TMSCustomFieldScreenDetailComponent } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen-detail.component';
import { TMSCustomFieldScreenService } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen.service';
import { TMSCustomFieldScreen } from '../../../../../../main/webapp/app/entities/tms-custom-field-screen/tms-custom-field-screen.model';

describe('Component Tests', () => {

    describe('TMSCustomFieldScreen Management Detail Component', () => {
        let comp: TMSCustomFieldScreenDetailComponent;
        let fixture: ComponentFixture<TMSCustomFieldScreenDetailComponent>;
        let service: TMSCustomFieldScreenService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [TMSCustomFieldScreenDetailComponent],
                providers: [
                    TMSCustomFieldScreenService
                ]
            })
            .overrideTemplate(TMSCustomFieldScreenDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TMSCustomFieldScreenDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TMSCustomFieldScreenService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new TMSCustomFieldScreen(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.tMSCustomFieldScreen).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
