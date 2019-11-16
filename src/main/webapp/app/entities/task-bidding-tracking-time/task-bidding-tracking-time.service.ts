import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TaskBiddingTrackingTime } from './task-bidding-tracking-time.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TaskBiddingTrackingTime>;

@Injectable()
export class TaskBiddingTrackingTimeService {

    private resourceUrl =  SERVER_API_URL + 'api/task-bidding-tracking-times';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/task-bidding-tracking-times';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(taskBiddingTrackingTime: TaskBiddingTrackingTime): Observable<EntityResponseType> {
        const copy = this.convert(taskBiddingTrackingTime);
        return this.http.post<TaskBiddingTrackingTime>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(taskBiddingTrackingTime: TaskBiddingTrackingTime): Observable<EntityResponseType> {
        const copy = this.convert(taskBiddingTrackingTime);
        return this.http.put<TaskBiddingTrackingTime>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TaskBiddingTrackingTime>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TaskBiddingTrackingTime[]>> {
        const options = createRequestOption(req);
        return this.http.get<TaskBiddingTrackingTime[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TaskBiddingTrackingTime[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<TaskBiddingTrackingTime[]>> {
        const options = createRequestOption(req);
        return this.http.get<TaskBiddingTrackingTime[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TaskBiddingTrackingTime[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TaskBiddingTrackingTime = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TaskBiddingTrackingTime[]>): HttpResponse<TaskBiddingTrackingTime[]> {
        const jsonResponse: TaskBiddingTrackingTime[] = res.body;
        const body: TaskBiddingTrackingTime[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TaskBiddingTrackingTime.
     */
    private convertItemFromServer(taskBiddingTrackingTime: TaskBiddingTrackingTime): TaskBiddingTrackingTime {
        const copy: TaskBiddingTrackingTime = Object.assign({}, taskBiddingTrackingTime);
        copy.startTime = this.dateUtils
            .convertDateTimeFromServer(taskBiddingTrackingTime.startTime);
        copy.endTime = this.dateUtils
            .convertDateTimeFromServer(taskBiddingTrackingTime.endTime);
        return copy;
    }

    /**
     * Convert a TaskBiddingTrackingTime to a JSON which can be sent to the server.
     */
    private convert(taskBiddingTrackingTime: TaskBiddingTrackingTime): TaskBiddingTrackingTime {
        const copy: TaskBiddingTrackingTime = Object.assign({}, taskBiddingTrackingTime);

        copy.startTime = this.dateUtils.toDate(taskBiddingTrackingTime.startTime);

        copy.endTime = this.dateUtils.toDate(taskBiddingTrackingTime.endTime);
        return copy;
    }
}
