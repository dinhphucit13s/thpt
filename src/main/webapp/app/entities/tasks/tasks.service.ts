import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Tasks } from './tasks.model';
import { createRequestOption } from '../../shared';
import {TaskBidding} from '../task-bidding/task-bidding.model';

export type EntityResponseType = HttpResponse<Tasks>;

@Injectable()
export class TasksService {

    private resourceUrl =  SERVER_API_URL + 'api/tasks';
    private resourceBiddingTaskUrl =  SERVER_API_URL + 'api/tasks-bidding';
    private resourceUrlBugTrackingTasks =  SERVER_API_URL + 'api/tasks/bug-tracking-task';
    private resourceUrlRatioBug =  SERVER_API_URL + 'api/tasks/ratio-bug';
    private resourceUrlBugTrackingMember =  SERVER_API_URL + 'api/tasks/tracking-member';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/tasks';
    private resourceUrlTaskUnAssign = SERVER_API_URL + 'api/task/task-un-assign';
    private resourceUrlListCloneTasks = SERVER_API_URL + 'api/task/task-list-clone';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(tasks: Tasks): Observable<EntityResponseType> {
        const copy = this.convert(tasks);
        return this.http.post<Tasks>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(tasks: Tasks): Observable<EntityResponseType> {
        const copy = this.convert(tasks);
        return this.http.put<Tasks>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Tasks>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any, packId?: any): Observable<HttpResponse<Tasks[]>> {
        const options = createRequestOption(req);
        if (packId !== null && packId !== undefined) {
            return this.http.get<Tasks[]>(`${this.resourceUrl}?packageId=${packId}`, { params: options, observe: 'response' })
                .map((res: HttpResponse<Tasks[]>) => this.convertArrayResponse(res));
        } else {
            return this.http.get<Tasks[]>(this.resourceUrl, { params: options, observe: 'response' })
                .map((res: HttpResponse<Tasks[]>) => this.convertArrayResponse(res));
        }
    }

    queryTaskUnAssign(req?: any): Observable<HttpResponse<Tasks[]>> {
        const options = createRequestOption(req);
        return this.http.get<Tasks[]>(this.resourceUrlTaskUnAssign, {params: options, observe: 'response' })
            .map((res: HttpResponse<Tasks[]>) => this.convertArrayResponse(res));
    }

    // PhuVD3
    getTasksListClone(req?: any, packId?: any): Observable<HttpResponse<Tasks[]>> {
        const options = createRequestOption(req);
        return this.http.get<Tasks[]>(`${this.resourceUrlListCloneTasks}?packageId=${packId}`, {params: options, observe: 'response' })
            .map((res: HttpResponse<Tasks[]>) => this.convertArrayResponse(res));
    }

    // PhuVD3 move Tasks
    moveTasks(payload: any, pacId: any): Observable<HttpResponse<Tasks[]>> {
        return this.http.post<Tasks[]>(`${this.resourceUrl}/moveTasks/${pacId}`, payload, { observe: 'response' })
            .map((res: HttpResponse<Tasks[]>) => this.convertArrayResponse(res));
    }

    queryBugTrackingTasks(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrlBugTrackingTasks}`, {params: options, observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    queryRatioTaskHasBug(req?: any): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrlRatioBug}`, {params: options, observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    queryBugTrackingMember(req?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrlBugTrackingMember}`, {params: options, observe: 'response' })
            .map((res: HttpResponse<any>) => {
                return res;
            });
    }

    loadTaskColumn(id: any): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/fields/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    loadTaskDynamicColumn(id: any): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/dynamic-fields/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Tasks[]>> {
        const options = createRequestOption(req);
        return this.http.get<Tasks[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Tasks[]>) => this.convertArrayResponse(res));
    }

    import(formData: FormData): Observable<EntityResponseType> {
        return this.http.post<any>(`${this.resourceUrl}/import`, formData, { observe: 'response' })
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Tasks = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Tasks[]>): HttpResponse<Tasks[]> {
        const jsonResponse: Tasks[] = res.body;
        const body: Tasks[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Tasks.
     */
    private convertItemFromServer(tasks: Tasks): Tasks {
        const copy: Tasks = Object.assign({}, tasks);
        copy.estimateStartTime = this.dateUtils
            .convertDateTimeFromServer(tasks.estimateStartTime);
        copy.estimateEndTime = this.dateUtils
            .convertDateTimeFromServer(tasks.estimateEndTime);
        copy.opStartTime = this.dateUtils
            .convertDateTimeFromServer(tasks.opStartTime);
        copy.opEndTime = this.dateUtils
            .convertDateTimeFromServer(tasks.opEndTime);
        copy.review1StartTime = this.dateUtils
            .convertDateTimeFromServer(tasks.review1StartTime);
        copy.review1EndTime = this.dateUtils
            .convertDateTimeFromServer(tasks.review1EndTime);
        copy.fixStartTime = this.dateUtils
            .convertDateTimeFromServer(tasks.fixStartTime);
        copy.fixEndTime = this.dateUtils
            .convertDateTimeFromServer(tasks.fixEndTime);
        copy.review2StartTime = this.dateUtils
            .convertDateTimeFromServer(tasks.review2StartTime);
        copy.review2EndTime = this.dateUtils
            .convertDateTimeFromServer(tasks.review2EndTime);
        copy.fiStartTime = this.dateUtils
            .convertDateTimeFromServer(tasks.fiStartTime);
        copy.fiEndTime = this.dateUtils
            .convertDateTimeFromServer(tasks.fiEndTime);
        copy.duration = this.convertDuration(tasks.duration);
        return copy;
    }

    /**
     * Convert a Tasks to a JSON which can be sent to the server.
     */
    private convert(tasks: Tasks): Tasks {
        const copy: Tasks = Object.assign({}, tasks);

        copy.estimateStartTime = this.dateUtils.toDate(tasks.estimateStartTime);

        copy.estimateEndTime = this.dateUtils.toDate(tasks.estimateEndTime);

        copy.opStartTime = this.dateUtils.toDate(tasks.opStartTime);

        copy.opEndTime = this.dateUtils.toDate(tasks.opEndTime);

        copy.review1StartTime = this.dateUtils.toDate(tasks.review1StartTime);

        copy.review1EndTime = this.dateUtils.toDate(tasks.review1EndTime);

        copy.fixStartTime = this.dateUtils.toDate(tasks.fixStartTime);

        copy.fixEndTime = this.dateUtils.toDate(tasks.fixEndTime);

        copy.review2StartTime = this.dateUtils.toDate(tasks.review2StartTime);

        copy.review2EndTime = this.dateUtils.toDate(tasks.review2EndTime);

        copy.fiStartTime = this.dateUtils.toDate(tasks.fiStartTime);

        copy.fiEndTime = this.dateUtils.toDate(tasks.fiEndTime);

        copy.duration = this.converDurationToTimeStamp(tasks.duration);
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
