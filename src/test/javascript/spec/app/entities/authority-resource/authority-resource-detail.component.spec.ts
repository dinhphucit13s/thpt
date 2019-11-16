/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { AuthorityResourceDetailComponent } from '../../../../../../main/webapp/app/entities/authority-resource/authority-resource-detail.component';
import { AuthorityResourceService } from '../../../../../../main/webapp/app/entities/authority-resource/authority-resource.service';
import { AuthorityResource } from '../../../../../../main/webapp/app/entities/authority-resource/authority-resource.model';

describe('Component Tests', () => {

    describe('AuthorityResource Management Detail Component', () => {
        let comp: AuthorityResourceDetailComponent;
        let fixture: ComponentFixture<AuthorityResourceDetailComponent>;
        let service: AuthorityResourceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [AuthorityResourceDetailComponent],
                providers: [
                    AuthorityResourceService
                ]
            })
            .overrideTemplate(AuthorityResourceDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AuthorityResourceDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuthorityResourceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new AuthorityResource(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.authorityResource).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
