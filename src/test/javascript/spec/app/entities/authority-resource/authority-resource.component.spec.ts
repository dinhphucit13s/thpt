/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { AuthorityResourceComponent } from '../../../../../../main/webapp/app/entities/authority-resource/authority-resource.component';
import { AuthorityResourceService } from '../../../../../../main/webapp/app/entities/authority-resource/authority-resource.service';
import { AuthorityResource } from '../../../../../../main/webapp/app/entities/authority-resource/authority-resource.model';

describe('Component Tests', () => {

    describe('AuthorityResource Management Component', () => {
        let comp: AuthorityResourceComponent;
        let fixture: ComponentFixture<AuthorityResourceComponent>;
        let service: AuthorityResourceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [AuthorityResourceComponent],
                providers: [
                    AuthorityResourceService
                ]
            })
            .overrideTemplate(AuthorityResourceComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AuthorityResourceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AuthorityResourceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new AuthorityResource(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.authorityResources[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
