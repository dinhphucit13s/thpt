import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { TaskBidding } from './task-bidding.model';
import { createRequestOption } from '../../shared';
import {Tasks} from '../tasks/tasks.model';
import {JhiDateUtils} from 'ng-jhipster';
import {DatePipe} from '@angular/common';

export type EntityResponseType = HttpResponse<TaskBidding>;

@Injectable()
export class TaskBiddingService {

    private resourceUrl =  SERVER_API_URL + 'api/task-biddings';
    private resourceBiddingTaskUrl = SERVER_API_URL + 'api/task-biddings-mode';
    private resourceUnBiddingTaskUrl = SERVER_API_URL + 'api/task-un-biddings';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/task-biddings';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils, private  datePipe: DatePipe) { }

    getListTaskBiddingByMode(req?: any): Observable<HttpResponse<Tasks[]>> {
        const options = createRequestOption(req);
        return this.http.get<Tasks[]>(this.resourceBiddingTaskUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Tasks[]>) => this.convertArrayResponse(res));
    }

    unBiddingTask(idList: number): Observable<HttpResponse<any>> {
        return this.http.put<any>(this.resourceUnBiddingTaskUrl, idList, { observe: 'response'});
    }

    getFieldConfig(projectId: number):  Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/fields/${projectId}`, { observe: 'response' });
    }

    create(taskBidding: TaskBidding): Observable<EntityResponseType> {
        const copy = this.convert(taskBidding);
        return this.http.post<TaskBidding>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(taskBidding: TaskBidding): Observable<EntityResponseType> {
        const copy = this.convert(taskBidding);
        return this.http.put<TaskBidding>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TaskBidding>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TaskBidding[]>> {
        const options = createRequestOption(req);
        return this.http.get<TaskBidding[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TaskBidding[]>) => this.convertArrayResponse(res));
    }

    biddingTask(payload: any, classify: any): Observable<HttpResponse<TaskBidding[]>> {
        return this.http.post<TaskBidding[]>(`${this.resourceUrl}/create/${classify}`, payload, { observe: 'response' })
            .map((res: HttpResponse<TaskBidding[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<TaskBidding[]>> {
        const options = createRequestOption(req);
        return this.http.get<TaskBidding[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TaskBidding[]>) => this.convertArrayResponse(res));
    }

    getMultiWorkFlowOfPurchaseOrders(projectId: any, poId: any):  Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/multi-fields?projectId=${projectId}&purchaseOrderId=${poId}`, { observe: 'response' });
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TaskBidding = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TaskBidding[]>): HttpResponse<TaskBidding[]> {
        const jsonResponse: TaskBidding[] = res.body;
        const body: TaskBidding[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TaskBidding.
     */
    private convertItemFromServer(taskBidding: TaskBidding): TaskBidding {
        const copy: TaskBidding = Object.assign({}, taskBidding);
        copy.task.estimateStartTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.estimateStartTime);
        copy.task.estimateEndTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.estimateEndTime);
        copy.task.opStartTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.opStartTime);
        copy.task.opEndTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.opEndTime);
        copy.task.review1StartTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.review1StartTime);
        copy.task.review1EndTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.review1EndTime);
        copy.task.fixStartTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.fixStartTime);
        copy.task.fixEndTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.fixEndTime);
        copy.task.review2StartTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.review2StartTime);
        copy.task.review2EndTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.review2EndTime);
        copy.task.fiStartTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.fiStartTime);
        copy.task.fiEndTime = this.dateUtils
            .convertDateTimeFromServer(taskBidding.task.fiEndTime);
        copy.task.duration = this.convertDuration(taskBidding.task.duration);
        return copy;
    }

    /**
     * Convert a TaskBidding to a JSON which can be sent to the server.
     */
    private convert(taskBidding: TaskBidding): TaskBidding {
        const copy: TaskBidding = Object.assign({}, taskBidding);

        copy.task.estimateStartTime = this.dateUtils.toDate(taskBidding.task.estimateStartTime);

        copy.task.estimateEndTime = this.dateUtils.toDate(taskBidding.task.estimateEndTime);

        copy.task.opStartTime = this.dateUtils.toDate(taskBidding.task.opStartTime);

        copy.task.opEndTime = this.dateUtils.toDate(taskBidding.task.opEndTime);

        copy.task.review1StartTime = this.dateUtils.toDate(taskBidding.task.review1StartTime);

        copy.task.review1EndTime = this.dateUtils.toDate(taskBidding.task.review1EndTime);

        copy.task.fixStartTime = this.dateUtils.toDate(taskBidding.task.fixStartTime);

        copy.task.fixEndTime = this.dateUtils.toDate(taskBidding.task.fixEndTime);

        copy.task.review2StartTime = this.dateUtils.toDate(taskBidding.task.review2StartTime);

        copy.task.review2EndTime = this.dateUtils.toDate(taskBidding.task.review2EndTime);

        copy.task.fiStartTime = this.dateUtils.toDate(taskBidding.task.fiStartTime);

        copy.task.fiEndTime = this.dateUtils.toDate(taskBidding.task.fiEndTime);

        copy.task.duration = this.converDurationToTimeStamp(taskBidding.task.duration);
        return copy;
    }

    private convertDuration(duration: any) {
        let durationMinute = parseInt(duration, 10);

        let weeks = Math.floor(durationMinute / (60 * 8 * 5)).toFixed(0);
        weeks = parseInt(weeks, 10) > 0 ? weeks + 'w' : '';
        durationMinute = durationMinute % (60 * 8 * 5);

        let days = Math.floor(durationMinute / (60 * 8)).toFixed(0);
        days = parseInt(days, 10) > 0 ? days + 'd' : '';
        durationMinute = durationMinute % (60 * 8);

        let hours = Math.floor(durationMinute / 60).toFixed(0);
        hours = parseInt(hours, 10) > 0 ? hours + 'h' : '';

        let minutes = Math.floor(durationMinute % 60).toFixed(0);
        minutes = parseInt(minutes, 10) > 0 ? minutes + 'm' : '';
        return weeks + days + hours + minutes;
    }

    private converDurationToTimeStamp(duration: any) {
        const weeks = this.getNumberOfTime(duration, 'w') * 60 * 8 * 5;
        const days = this.getNumberOfTime(duration, 'd') * 60 * 8;
        const hours = this.getNumberOfTime(duration, 'h') * 60;
        const mins = this.getNumberOfTime(duration, 'm');
        return weeks + days + hours + mins;
    }

    private getNumberOfTime(duration: any, condition: any) {
        if (duration) {
            const regEx = new RegExp('\\d*' + condition, 'g');
            const time = duration.match(regEx);
            if (time) {
                return parseInt(time[0].replace(condition, ''), 0);
            }
        }
        return 0;
    }
}
