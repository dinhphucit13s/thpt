import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { DtmsMonitoring } from './dtms-monitoring.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<DtmsMonitoring>;

@Injectable()
export class DtmsMonitoringService {

    private resourceUrl =  SERVER_API_URL + 'api/dtms-monitorings';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/dtms-monitorings';

    constructor(private http: HttpClient) { }

    create(dtmsMonitoring: DtmsMonitoring): Observable<EntityResponseType> {
        const copy = this.convert(dtmsMonitoring);
        return this.http.post<DtmsMonitoring>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(dtmsMonitoring: DtmsMonitoring): Observable<EntityResponseType> {
        const copy = this.convert(dtmsMonitoring);
        return this.http.put<DtmsMonitoring>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<DtmsMonitoring>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<DtmsMonitoring[]>> {
        const options = createRequestOption(req);
        return this.http.get<DtmsMonitoring[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DtmsMonitoring[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<DtmsMonitoring[]>> {
        const options = createRequestOption(req);
        return this.http.get<DtmsMonitoring[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DtmsMonitoring[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: DtmsMonitoring = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<DtmsMonitoring[]>): HttpResponse<DtmsMonitoring[]> {
        const jsonResponse: DtmsMonitoring[] = res.body;
        const body: DtmsMonitoring[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to DtmsMonitoring.
     */
    private convertItemFromServer(dtmsMonitoring: DtmsMonitoring): DtmsMonitoring {
        const copy: DtmsMonitoring = Object.assign({}, dtmsMonitoring);
        return copy;
    }

    /**
     * Convert a DtmsMonitoring to a JSON which can be sent to the server.
     */
    private convert(dtmsMonitoring: DtmsMonitoring): DtmsMonitoring {
        const copy: DtmsMonitoring = Object.assign({}, dtmsMonitoring);
        return copy;
    }
}
