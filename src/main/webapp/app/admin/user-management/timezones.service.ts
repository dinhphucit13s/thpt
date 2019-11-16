import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { TimeZones } from './timezones.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TimeZones>;

@Injectable()
export class TimeZoneService {

    private resourceUrl =  SERVER_API_URL + 'api/timezones';

    constructor(private http: HttpClient) { }

    find(id: String): Observable<EntityResponseType> {
        return this.http.get<TimeZones>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TimeZones[]>> {
        const options = createRequestOption(req);
        return this.http.get<TimeZones[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TimeZones[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TimeZones = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TimeZones[]>): HttpResponse<TimeZones[]> {
        const jsonResponse: TimeZones[] = res.body;
        const body: TimeZones[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to timezones.
     */
    private convertItemFromServer(timezones: TimeZones): TimeZones {
        const copy: TimeZones = Object.assign({}, timezones);
        return copy;
    }

    /**
     * Convert a ProcessSchedules to a JSON which can be sent to the server.
     */
    private convert(timezones: TimeZones): TimeZones {
        const copy: TimeZones = Object.assign({}, timezones);
        return copy;
    }
}
