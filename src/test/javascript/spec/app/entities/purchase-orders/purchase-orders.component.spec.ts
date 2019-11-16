/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DtmsTestModule } from '../../../test.module';
import { PurchaseOrdersComponent } from '../../../../../../main/webapp/app/entities/purchase-orders/purchase-orders.component';
import { PurchaseOrdersService } from '../../../../../../main/webapp/app/entities/purchase-orders/purchase-orders.service';
import { PurchaseOrders } from '../../../../../../main/webapp/app/entities/purchase-orders/purchase-orders.model';

describe('Component Tests', () => {

    describe('PurchaseOrders Management Component', () => {
        let comp: PurchaseOrdersComponent;
        let fixture: ComponentFixture<PurchaseOrdersComponent>;
        let service: PurchaseOrdersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [PurchaseOrdersComponent],
                providers: [
                    PurchaseOrdersService
                ]
            })
            .overrideTemplate(PurchaseOrdersComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PurchaseOrdersComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseOrdersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new PurchaseOrders(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.purchaseOrders[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
