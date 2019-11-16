import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';
import {PurchaseOrders} from '../../../entities/purchase-orders/purchase-orders.model';
import {Selects} from './select.model';
import {createRequestOption} from '../../index';

export type EntityResponseType = HttpResponse<Selects>;

@Injectable()
export class SelectService {

    private resourceUrl;

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    query(req?: any, urlOption?: any): Observable<HttpResponse<Selects[]>> {
        this.resourceUrl = SERVER_API_URL + urlOption;
        const options = createRequestOption(req);
        if (req !== undefined) {
            return this.http.get<Selects[]>(this.resourceUrl,  {params: options, observe: 'response' });
        } else {
            return this.http.get<Selects[]>(this.resourceUrl, { observe: 'response' });
        }
    }
}
