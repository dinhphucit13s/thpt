import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { BusinessUnit } from './business-unit.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<BusinessUnit>;

@Injectable()
export class BusinessUnitService {

    private resourceUrl =  SERVER_API_URL + 'api/business-units';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/business-units';

    constructor(private http: HttpClient) { }

    create(businessUnit: BusinessUnit): Observable<EntityResponseType> {
        const copy = this.convert(businessUnit);
        return this.http.post<BusinessUnit>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(businessUnit: BusinessUnit): Observable<EntityResponseType> {
        const copy = this.convert(businessUnit);
        return this.http.put<BusinessUnit>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<BusinessUnit>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<BusinessUnit[]>> {
        const options = createRequestOption(req);
        return this.http.get<BusinessUnit[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BusinessUnit[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<BusinessUnit[]>> {
        const options = createRequestOption(req);
        return this.http.get<BusinessUnit[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BusinessUnit[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: BusinessUnit = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<BusinessUnit[]>): HttpResponse<BusinessUnit[]> {
        const jsonResponse: BusinessUnit[] = res.body;
        const body: BusinessUnit[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to BusinessUnit.
     */
    private convertItemFromServer(businessUnit: BusinessUnit): BusinessUnit {
        const copy: BusinessUnit = Object.assign({}, businessUnit);
        return copy;
    }

    /**
     * Convert a BusinessUnit to a JSON which can be sent to the server.
     */
    private convert(businessUnit: BusinessUnit): BusinessUnit {
        const copy: BusinessUnit = Object.assign({}, businessUnit);
        return copy;
    }
}
