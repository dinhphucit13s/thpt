import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TaskTrackingTime } from './task-tracking-time.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TaskTrackingTime>;

@Injectable()
export class TaskTrackingTimeService {

    private resourceUrl =  SERVER_API_URL + 'api/task-tracking-times';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/task-tracking-times';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(taskTrackingTime: TaskTrackingTime): Observable<EntityResponseType> {
        const copy = this.convert(taskTrackingTime);
        return this.http.post<TaskTrackingTime>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(taskTrackingTime: TaskTrackingTime): Observable<EntityResponseType> {
        const copy = this.convert(taskTrackingTime);
        return this.http.put<TaskTrackingTime>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TaskTrackingTime>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TaskTrackingTime[]>> {
        const options = createRequestOption(req);
        return this.http.get<TaskTrackingTime[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TaskTrackingTime[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<TaskTrackingTime[]>> {
        const options = createRequestOption(req);
        return this.http.get<TaskTrackingTime[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TaskTrackingTime[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TaskTrackingTime = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TaskTrackingTime[]>): HttpResponse<TaskTrackingTime[]> {
        const jsonResponse: TaskTrackingTime[] = res.body;
        const body: TaskTrackingTime[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TaskTrackingTime.
     */
    private convertItemFromServer(taskTrackingTime: TaskTrackingTime): TaskTrackingTime {
        const copy: TaskTrackingTime = Object.assign({}, taskTrackingTime);
        copy.startTime = this.dateUtils
            .convertDateTimeFromServer(taskTrackingTime.startTime);
        copy.endTime = this.dateUtils
            .convertDateTimeFromServer(taskTrackingTime.endTime);
        return copy;
    }

    /**
     * Convert a TaskTrackingTime to a JSON which can be sent to the server.
     */
    private convert(taskTrackingTime: TaskTrackingTime): TaskTrackingTime {
        const copy: TaskTrackingTime = Object.assign({}, taskTrackingTime);

        copy.startTime = this.dateUtils.toDate(taskTrackingTime.startTime);

        copy.endTime = this.dateUtils.toDate(taskTrackingTime.endTime);
        return copy;
    }
}
