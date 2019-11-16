import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { TmsPost } from './tms-post.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TmsPost>;

@Injectable()
export class TmsPostService {

    private resourceUrl =  SERVER_API_URL + 'api/tms-posts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/tms-posts';

    constructor(private http: HttpClient) { }

    create(tmsPost: TmsPost): Observable<EntityResponseType> {
        const copy = this.convert(tmsPost);
        return this.http.post<TmsPost>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(tmsPost: TmsPost): Observable<EntityResponseType> {
        const copy = this.convert(tmsPost);
        return this.http.put<TmsPost>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TmsPost>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TmsPost[]>> {
        const options = createRequestOption(req);
        return this.http.get<TmsPost[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TmsPost[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<TmsPost[]>> {
        const options = createRequestOption(req);
        return this.http.get<TmsPost[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TmsPost[]>) => this.convertArrayResponse(res));
    }

    getAnswerOfThread(req?: any, threadId?: number, exceptId?: number): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrl}/threads/${threadId}/${exceptId}`, { params: options, observe: 'response' });
    }

    updatePosts(formData: any): Observable<HttpResponse<TmsPost>> {
        return this.http.put<TmsPost>(this.resourceUrl + '/update', formData, { observe: 'response' });
    }

    createPosts(formData: any): Observable<HttpResponse<TmsPost>> {
        return this.http.post<TmsPost>(this.resourceUrl + '/create', formData, { observe: 'response' });
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TmsPost = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TmsPost[]>): HttpResponse<TmsPost[]> {
        const jsonResponse: TmsPost[] = res.body;
        const body: TmsPost[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TmsPost.
     */
    private convertItemFromServer(tmsPost: TmsPost): TmsPost {
        const copy: TmsPost = Object.assign({}, tmsPost);
        return copy;
    }

    /**
     * Convert a TmsPost to a JSON which can be sent to the server.
     */
    private convert(tmsPost: TmsPost): TmsPost {
        const copy: TmsPost = Object.assign({}, tmsPost);
        return copy;
    }
}
