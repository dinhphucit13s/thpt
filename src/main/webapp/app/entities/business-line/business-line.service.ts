import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { BusinessLine } from './business-line.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<BusinessLine>;

@Injectable()
export class BusinessLineService {

    private resourceUrl =  SERVER_API_URL + 'api/business-lines';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/business-lines';

    constructor(private http: HttpClient) { }

    create(businessLine: BusinessLine): Observable<EntityResponseType> {
        const copy = this.convert(businessLine);
        return this.http.post<BusinessLine>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(businessLine: BusinessLine): Observable<EntityResponseType> {
        const copy = this.convert(businessLine);
        return this.http.put<BusinessLine>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<BusinessLine>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<BusinessLine[]>> {
        const options = createRequestOption(req);
        return this.http.get<BusinessLine[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BusinessLine[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<BusinessLine[]>> {
        const options = createRequestOption(req);
        return this.http.get<BusinessLine[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BusinessLine[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: BusinessLine = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<BusinessLine[]>): HttpResponse<BusinessLine[]> {
        const jsonResponse: BusinessLine[] = res.body;
        const body: BusinessLine[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to BusinessLine.
     */
    private convertItemFromServer(businessLine: BusinessLine): BusinessLine {
        const copy: BusinessLine = Object.assign({}, businessLine);
        return copy;
    }

    /**
     * Convert a BusinessLine to a JSON which can be sent to the server.
     */
    private convert(businessLine: BusinessLine): BusinessLine {
        const copy: BusinessLine = Object.assign({}, businessLine);
        return copy;
    }
}
