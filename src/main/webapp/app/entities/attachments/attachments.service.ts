import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Attachments } from './attachments.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Attachments>;

@Injectable()
export class AttachmentsService {

    private resourceUrl =  SERVER_API_URL + 'api/attachments';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/attachments';

    constructor(private http: HttpClient) { }

    create(attachments: Attachments): Observable<EntityResponseType> {
        const copy = this.convert(attachments);
        return this.http.post<Attachments>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(attachments: Attachments): Observable<EntityResponseType> {
        const copy = this.convert(attachments);
        return this.http.put<Attachments>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Attachments>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Attachments[]>> {
        const options = createRequestOption(req);
        return this.http.get<Attachments[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Attachments[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Attachments[]>> {
        const options = createRequestOption(req);
        return this.http.get<Attachments[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Attachments[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Attachments = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Attachments[]>): HttpResponse<Attachments[]> {
        const jsonResponse: Attachments[] = res.body;
        const body: Attachments[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Attachments.
     */
    private convertItemFromServer(attachments: Attachments): Attachments {
        const copy: Attachments = Object.assign({}, attachments);
        return copy;
    }

    /**
     * Convert a Attachments to a JSON which can be sent to the server.
     */
    private convert(attachments: Attachments): Attachments {
        const copy: Attachments = Object.assign({}, attachments);
        return copy;
    }
}
