import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { TmsThread } from './tms-thread.model';
import { createRequestOption } from '../../shared';
import {map} from 'rxjs/operators';

export type EntityResponseType = HttpResponse<TmsThread>;

@Injectable()
export class TmsThreadService {

    private resourceUrl =  SERVER_API_URL + 'api/tms-threads';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/tms-threads';

    constructor(private http: HttpClient) { }

    create(tmsThread: TmsThread): Observable<EntityResponseType> {
        const copy = this.convert(tmsThread);
        return this.http.post<TmsThread>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(tmsThread: TmsThread): Observable<EntityResponseType> {
        const copy = this.convert(tmsThread);
        return this.http.put<TmsThread>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TmsThread>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    // getThreadById(threadId: any): Observable<HttpResponse<any>> {
    //     return this.http.get<any>(this.resourceUrl + '/' + threadId, { observe: 'response' });
    // }

    query(req?: any): Observable<HttpResponse<TmsThread[]>> {
        const options = createRequestOption(req);
        return this.http.get<TmsThread[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TmsThread[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<TmsThread[]>> {
        const options = createRequestOption(req);
        return this.http.get<TmsThread[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TmsThread[]>) => this.convertArrayResponse(res));
    }

    loadListQuestionAndAnswer(req?: any, projectId?: number, filter?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrl}/${projectId}/${filter}`, { params: options, observe: 'response' });
    }

    updateViewsOfThread(threadId: any): Observable<HttpResponse<any>> {
        return this.http.put<any>(this.resourceUrl + '/updateViews?threadId=' + threadId, { observe: 'response' });
    }

    createQuestionAndAnswer(formData: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.resourceUrl + '/create', formData, { observe: 'response' })
            .pipe(map((res: HttpResponse<any>) => res));
    }

    changeTitleOfThread(threadId: any, threadTitle): Observable<HttpResponse<any>> {
        return this.http.put<any>(`${this.resourceUrl}/changeTitleOfThread?id=${threadId}&title=${threadTitle}`, { observe: 'response' });
    }

    changeThreadToClose(threadId: any): Observable<HttpResponse<any>> {
        return this.http.put<any>(this.resourceUrl + '/changeThreadToClose?threadId=' + threadId, { observe: 'response' });
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TmsThread = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TmsThread[]>): HttpResponse<TmsThread[]> {
        const jsonResponse: TmsThread[] = res.body;
        const body: TmsThread[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TmsThread.
     */
    private convertItemFromServer(tmsThread: TmsThread): TmsThread {
        const copy: TmsThread = Object.assign({}, tmsThread);
        return copy;
    }

    /**
     * Convert a TmsThread to a JSON which can be sent to the server.
     */
    private convert(tmsThread: TmsThread): TmsThread {
        const copy: TmsThread = Object.assign({}, tmsThread);
        return copy;
    }
}
