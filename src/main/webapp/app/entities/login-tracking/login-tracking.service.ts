import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { LoginTracking } from './login-tracking.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<LoginTracking>;

@Injectable()
export class LoginTrackingService {

    private resourceUrl =  SERVER_API_URL + 'api/login-trackings';
    private resourceStartTimeLoginUrl =  SERVER_API_URL + 'api/login-tracking-start-time';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/login-trackings';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(loginTracking: LoginTracking): Observable<EntityResponseType> {
        const copy = this.convert(loginTracking);
        return this.http.post<LoginTracking>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(loginTracking: LoginTracking): Observable<EntityResponseType> {
        const copy = this.convert(loginTracking);
        return this.http.put<LoginTracking>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<LoginTracking>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    queryStartTimeLogin(userLogin?: any, date?: any): Observable<EntityResponseType> {
        return this.http.get<LoginTracking>(`${this.resourceStartTimeLoginUrl}?userLogin=${userLogin}&date=${date}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<LoginTracking[]>> {
        const options = createRequestOption(req);
        return this.http.get<LoginTracking[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<LoginTracking[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<LoginTracking[]>> {
        const options = createRequestOption(req);
        return this.http.get<LoginTracking[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<LoginTracking[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: LoginTracking = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<LoginTracking[]>): HttpResponse<LoginTracking[]> {
        const jsonResponse: LoginTracking[] = res.body;
        const body: LoginTracking[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to LoginTracking.
     */
    private convertItemFromServer(loginTracking: LoginTracking): LoginTracking {
        const copy: LoginTracking = Object.assign({}, loginTracking);
        copy.startTime = this.dateUtils
            .convertDateTimeFromServer(loginTracking.startTime);
        copy.endTime = this.dateUtils
            .convertDateTimeFromServer(loginTracking.endTime);
        return copy;
    }

    /**
     * Convert a LoginTracking to a JSON which can be sent to the server.
     */
    private convert(loginTracking: LoginTracking): LoginTracking {
        const copy: LoginTracking = Object.assign({}, loginTracking);

        copy.startTime = this.dateUtils.toDate(loginTracking.startTime);

        copy.endTime = this.dateUtils.toDate(loginTracking.endTime);
        return copy;
    }
}
