/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DtmsTestModule } from '../../../test.module';
import { PurchaseOrdersDetailComponent } from '../../../../../../main/webapp/app/entities/purchase-orders/purchase-orders-detail.component';
import { PurchaseOrdersService } from '../../../../../../main/webapp/app/entities/purchase-orders/purchase-orders.service';
import { PurchaseOrders } from '../../../../../../main/webapp/app/entities/purchase-orders/purchase-orders.model';

describe('Component Tests', () => {

    describe('PurchaseOrders Management Detail Component', () => {
        let comp: PurchaseOrdersDetailComponent;
        let fixture: ComponentFixture<PurchaseOrdersDetailComponent>;
        let service: PurchaseOrdersService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DtmsTestModule],
                declarations: [PurchaseOrdersDetailComponent],
                providers: [
                    PurchaseOrdersService
                ]
            })
            .overrideTemplate(PurchaseOrdersDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PurchaseOrdersDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseOrdersService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new PurchaseOrders(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.purchaseOrders).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
