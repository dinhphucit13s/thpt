import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Projects } from '../projects/projects.model';
import { createRequestOption } from '../../shared';
import {BugListDefault} from '../bug-list-default/bug-list-default.model';
import {Tasks} from '../tasks/tasks.model';
import {TrackingManagement} from './tracking-management.model';

export type EntityResponseType = HttpResponse<Projects>;

@Injectable()
export class TrackingManagementService {

    private resourceUrl =  SERVER_API_URL + 'api/tasks';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    queryTasks(req?: any, packId?: any): Observable<HttpResponse<TrackingManagement[]>> {
        const options = createRequestOption(req);
            return this.http.get<TrackingManagement[]>(`${this.resourceUrl}?packageId=${packId}`, { params: options, observe: 'response' })
                .map((res: HttpResponse<TrackingManagement[]>) => this.convertArrayResponse(res));
    }

    private convertArrayResponse(res: HttpResponse<TrackingManagement[]>): HttpResponse<TrackingManagement[]> {
        const jsonResponse: TrackingManagement[] = res.body;
        const body: TrackingManagement[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    private convertItemFromServer(tasks: TrackingManagement): TrackingManagement {
        const copy: TrackingManagement = Object.assign({}, tasks);
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
        return copy;
    }

}
