import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { PurchaseOrders } from './purchase-orders.model';
import { createRequestOption } from '../../shared';
import {Packages} from '../packages/packages.model';

export type EntityResponseType = HttpResponse<PurchaseOrders>;

@Injectable()
export class PurchaseOrdersService {

    private resourceUrl =  SERVER_API_URL + 'api/purchase-orders';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/purchase-orders';
    private resourceJsonUrl = SERVER_API_URL + 'api/json/purchase-orders';
    private resourceGetPOByProIdUrl = SERVER_API_URL + 'api/purchase-orders-projectId';
    private resourceGetPOBidding = SERVER_API_URL + 'api/purchase-orders-bidding';
    private resourceGetPOWithMonitoringByProIdUrl = SERVER_API_URL + 'api/purchase-orders-monitoring-projectId';

    /* URL API Get PO on popup Clone Package and move task
    PhuVD3*/
    private resourceJsonUrlClone = SERVER_API_URL + 'api/purchase-orders-clone';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    getListPurchaseOrderBiddingTask(projectId: any, userLogin: any) {
        return this.http.get<PurchaseOrders[]>(`${this.resourceGetPOBidding}?projectId=${projectId}&userLogin=${userLogin}`, {observe: 'response'});
    }

    /*Function API Get PO on popup Clone Package and move task
    PhuVD3*/
    clone(req?: any, proId?: any): Observable<HttpResponse<PurchaseOrders[]>> {
        const options = createRequestOption(req);
        if (proId !== undefined) {
            return this.http.get<PurchaseOrders[]>(`${this.resourceJsonUrlClone}?projectId=${proId}`, {params: options, observe: 'response'})
                .map((res: HttpResponse<PurchaseOrders[]>) => this.convertArrayResponse(res));
        }
    }
    create(purchaseOrders: PurchaseOrders): Observable<EntityResponseType> {
        const copy = this.convert(purchaseOrders);
        return this.http.post<PurchaseOrders>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(purchaseOrders: PurchaseOrders): Observable<EntityResponseType> {
        const copy = this.convert(purchaseOrders);
        return this.http.put<PurchaseOrders>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<PurchaseOrders>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any, proId?: any): Observable<HttpResponse<PurchaseOrders[]>> {
        const options = createRequestOption(req);
        if (proId !== undefined) {
            return this.http.get<PurchaseOrders[]>(`${this.resourceGetPOByProIdUrl}?projectId=${proId}`, {params: options, observe: 'response'})
                .map((res: HttpResponse<PurchaseOrders[]>) => this.convertArrayResponse(res));
        }
    }

    queryPoWithMonitoringByProId(req?: any, proId?: any): Observable<HttpResponse<PurchaseOrders[]>> {
        const options = createRequestOption(req);
        return this.http.get<PurchaseOrders[]>(`${this.resourceGetPOWithMonitoringByProIdUrl}?projectId=${proId}`, { observe: 'response'})
            .map((res: HttpResponse<PurchaseOrders[]>) => this.convertArrayResponse(res));
    }

    queryjson(req?: any): Observable<HttpResponse<PurchaseOrders[]>> {
        const options = createRequestOption(req);
        return this.http.get<PurchaseOrders[]>(this.resourceJsonUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<PurchaseOrders[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<PurchaseOrders[]>> {
        const options = createRequestOption(req);
        return this.http.get<PurchaseOrders[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<PurchaseOrders[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: PurchaseOrders = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<PurchaseOrders[]>): HttpResponse<PurchaseOrders[]> {
        const jsonResponse: PurchaseOrders[] = res.body;
        const body: PurchaseOrders[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to PurchaseOrders.
     */
    private convertItemFromServer(purchaseOrders: PurchaseOrders): PurchaseOrders {
        const copy: PurchaseOrders = Object.assign({}, purchaseOrders);
        copy.startTime = this.dateUtils
            .convertDateTimeFromServer(purchaseOrders.startTime);
        copy.endTime = this.dateUtils
            .convertDateTimeFromServer(purchaseOrders.endTime);
        return copy;
    }

    /**
     * Convert a PurchaseOrders to a JSON which can be sent to the server.
     */
    private convert(purchaseOrders: PurchaseOrders): PurchaseOrders {
        const copy: PurchaseOrders = Object.assign({}, purchaseOrders);

        copy.startTime = this.dateUtils.toDate(purchaseOrders.startTime);

        copy.endTime = this.dateUtils.toDate(purchaseOrders.endTime);
        return copy;
    }
}
