import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { BusinessUnitManager } from './business-unit-manager.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<BusinessUnitManager>;

@Injectable()
export class BusinessUnitManagerService {

    private resourceUrl =  SERVER_API_URL + 'api/business-unit-managers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/business-unit-managers';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(businessUnitManager: BusinessUnitManager): Observable<EntityResponseType> {
        const copy = this.convert(businessUnitManager);
        return this.http.post<BusinessUnitManager>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(businessUnitManager: BusinessUnitManager): Observable<EntityResponseType> {
        const copy = this.convert(businessUnitManager);
        return this.http.put<BusinessUnitManager>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<BusinessUnitManager>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<BusinessUnitManager[]>> {
        const options = createRequestOption(req);
        return this.http.get<BusinessUnitManager[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BusinessUnitManager[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<BusinessUnitManager[]>> {
        const options = createRequestOption(req);
        return this.http.get<BusinessUnitManager[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BusinessUnitManager[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: BusinessUnitManager = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<BusinessUnitManager[]>): HttpResponse<BusinessUnitManager[]> {
        const jsonResponse: BusinessUnitManager[] = res.body;
        const body: BusinessUnitManager[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(jsonResponse[i]);
            // body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to BusinessUnitManager.
     */
    private convertItemFromServer(businessUnitManager: BusinessUnitManager): BusinessUnitManager {
        const copy: BusinessUnitManager = Object.assign({}, businessUnitManager);
        copy.startTime = this.dateUtils
            .convertLocalDateFromServer(businessUnitManager.startTime);
        copy.endTime = this.dateUtils
            .convertLocalDateFromServer(businessUnitManager.endTime);
        return copy;
    }

    /**
     * Convert a BusinessUnitManager to a JSON which can be sent to the server.
     */
    private convert(businessUnitManager: BusinessUnitManager): BusinessUnitManager {
        const copy: BusinessUnitManager = Object.assign({}, businessUnitManager);
        copy.startTime = this.dateUtils
            .toDate(businessUnitManager.startTime);
        copy.endTime = this.dateUtils
            .toDate(businessUnitManager.endTime);
        return copy;
    }
}
