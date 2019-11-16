import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import {Feedback} from './feedback.model';
import { createRequestOption } from '../../shared';
import {Mail} from "../mail";
import {Issues} from "../issues";

export type EntityResponseType = HttpResponse<Feedback>;

@Injectable()
export class FeedbackService {

    private resourceUrl =  SERVER_API_URL + 'api/issues';
    private resourceUrlAttach =  SERVER_API_URL + 'api/issues-find';
    private resourceUrlCampaign =  SERVER_API_URL + 'api/issues-campaign';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/issues';
    private resourceFeedback = SERVER_API_URL + 'api/feedback';

    constructor(private http: HttpClient) { }

    create(feedback: Feedback, attachments: any): Observable<EntityResponseType> {
        const copy = this.convert(feedback);
        const formData: FormData = new FormData();
        for (let i = 0; i < attachments.length; i++) {
            formData.append('attachments', attachments[i]);
        }
        formData.append('feedback', new Blob([JSON.stringify(copy)], {
            type: 'application/json'
        }));
        return this.http.post<Feedback>(this.resourceFeedback, formData, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(issues: Issues): Observable<EntityResponseType> {
        const copy = this.convert(issues);
        return this.http.put<Issues>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Feedback>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    findAttach(id: number): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrlAttach}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Issues[]>> {
        const options = createRequestOption(req);
        return this.http.get<Issues[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Issues[]>) => this.convertArrayResponse(res));
    }

    queryCampaign(req?: any): Observable<HttpResponse<Issues[]>> {
        const options = createRequestOption(req);
        return this.http.get<Issues[]>(this.resourceUrlCampaign, { params: options, observe: 'response' })
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

    getListFeedbackOfUser(req?: any, userLogin?: any, proId?: number): Observable<HttpResponse<Issues[]>> {
        const options = createRequestOption(req);
        return this.http.get<Issues[]>(`${this.resourceUrl}/${proId}/${userLogin}`, { params: options, observe: 'response' });
    }

    getAttachmentByIssuesId(issuesId: any): Observable<HttpResponse<any[]>> {
        console.log(issuesId);
        return this.http.get<any[]>(`${this.resourceUrl}/attachment?issuesId=${issuesId}`, { observe: 'response'});
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
