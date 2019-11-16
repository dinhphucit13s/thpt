import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import {CustomReports} from './custom-reports.model';

export type EntityResponseType = HttpResponse<CustomReports>;

@Injectable()
export class CustomReportsService {

    private resourceUrl =  SERVER_API_URL + 'api/customReports';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(customReports: CustomReports): Observable<EntityResponseType> {
        const copy = this.convert(customReports);
        return this.http.post<CustomReports>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => res);
    }

    update(customReports: CustomReports): Observable<EntityResponseType> {
        const copy = this.convert(customReports);
        return this.http.put<CustomReports>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => res);
    }

    query(pageName?: any, userLogin?: any): Observable<HttpResponse<CustomReports>> {
        return this.http.get<CustomReports>(`${this.resourceUrl}?pageName=${pageName}&userLogin=${userLogin}`, { observe: 'response' })
            .map((res: HttpResponse<CustomReports>) => res);
    }

    /**
     * Convert a PurchaseOrders to a JSON which can be sent to the server.
     */
    private convert(customReports: CustomReports): CustomReports {
        const copy: CustomReports = Object.assign({}, customReports);
        copy.value = JSON.stringify(copy.value);
        return copy;
    }
}
