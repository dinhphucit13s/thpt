import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { TMSLogHistory } from './tms-log-history.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TMSLogHistory>;

@Injectable()
export class TMSLogHistoryService {

    private resourceUrl =  SERVER_API_URL + 'api/tms-log-histories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/tms-log-histories';

    constructor(private http: HttpClient) { }

    create(tMSLogHistory: TMSLogHistory): Observable<EntityResponseType> {
        const copy = this.convert(tMSLogHistory);
        return this.http.post<TMSLogHistory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(tMSLogHistory: TMSLogHistory): Observable<EntityResponseType> {
        const copy = this.convert(tMSLogHistory);
        return this.http.put<TMSLogHistory>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TMSLogHistory>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TMSLogHistory[]>> {
        const options = createRequestOption(req);
        return this.http.get<TMSLogHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TMSLogHistory[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<TMSLogHistory[]>> {
        const options = createRequestOption(req);
        return this.http.get<TMSLogHistory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TMSLogHistory[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TMSLogHistory = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TMSLogHistory[]>): HttpResponse<TMSLogHistory[]> {
        const jsonResponse: TMSLogHistory[] = res.body;
        const body: TMSLogHistory[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TMSLogHistory.
     */
    private convertItemFromServer(tMSLogHistory: TMSLogHistory): TMSLogHistory {
        const copy: TMSLogHistory = Object.assign({}, tMSLogHistory);
        return copy;
    }

    /**
     * Convert a TMSLogHistory to a JSON which can be sent to the server.
     */
    private convert(tMSLogHistory: TMSLogHistory): TMSLogHistory {
        const copy: TMSLogHistory = Object.assign({}, tMSLogHistory);
        return copy;
    }
}
