import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Issues } from '../issues/issues.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Issues>;

@Injectable()
export class ViewFeedBackService {

    private resourceUrl =  SERVER_API_URL + 'api/issues';
    private resourceUrlMoney =  SERVER_API_URL + 'api/issues-campaign-money';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/issues';

    constructor(private http: HttpClient) { }

    create(issues: Issues): Observable<EntityResponseType> {
        const copy = this.convert(issues);
        return this.http.post<Issues>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }
    trackingTM_Campaign(money?: any): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrlMoney}?money=${money}`, { observe: 'response' })
            .map((res: HttpResponse<any>) => {
                return res;
            });
    }
    update(issues: Issues): Observable<EntityResponseType> {
        const copy = this.convert(issues);
        return this.http.put<Issues>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Issues>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Issues[]>> {
        const options = createRequestOption(req);
        return this.http.get<Issues[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Issues[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Issues[]>> {
        const options = createRequestOption(req);
        return this.http.get<Issues[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Issues[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Issues = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Issues[]>): HttpResponse<Issues[]> {
        const jsonResponse: Issues[] = res.body;
        const body: Issues[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Issues.
     */
    private convertItemFromServer(issues: Issues): Issues {
        const copy: Issues = Object.assign({}, issues);
        return copy;
    }

    /**
     * Convert a Issues to a JSON which can be sent to the server.
     */
    private convert(issues: Issues): Issues {
        const copy: Issues = Object.assign({}, issues);
        return copy;
    }
}
