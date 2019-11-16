import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Comments } from './comments.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Comments>;

@Injectable()
export class CommentsService {

    private resourceUrl =  SERVER_API_URL + 'api/comments';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/comments';

    constructor(private http: HttpClient) { }

    create(comments: Comments): Observable<EntityResponseType> {
        const copy = this.convert(comments);
        return this.http.post<Comments>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(comments: Comments): Observable<EntityResponseType> {
        const copy = this.convert(comments);
        return this.http.put<Comments>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Comments>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Comments[]>> {
        const options = createRequestOption(req);
        return this.http.get<Comments[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Comments[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Comments[]>> {
        const options = createRequestOption(req);
        return this.http.get<Comments[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Comments[]>) => this.convertArrayResponse(res));
    }

    getCommentByPostId(postId: any): Observable<HttpResponse<any>> {
        return this.http.get<any>(this.resourceUrl + '/posts/' + postId, { observe: 'response' });
    }

    createCommentOfPosts(formData: any): Observable<HttpResponse<any>> {
        return this.http.post<any>(this.resourceUrl + '/create', formData, { observe: 'response' });
    }

    updateComments(formData: any): Observable<HttpResponse<any>> {
        return this.http.put<any>(this.resourceUrl + '/update', formData, { observe: 'response' });
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Comments = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Comments[]>): HttpResponse<Comments[]> {
        const jsonResponse: Comments[] = res.body;
        const body: Comments[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Comments.
     */
    private convertItemFromServer(comments: Comments): Comments {
        const copy: Comments = Object.assign({}, comments);
        return copy;
    }

    /**
     * Convert a Comments to a JSON which can be sent to the server.
     */
    private convert(comments: Comments): Comments {
        const copy: Comments = Object.assign({}, comments);
        return copy;
    }
}
