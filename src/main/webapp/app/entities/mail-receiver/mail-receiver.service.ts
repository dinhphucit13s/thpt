import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { MailReceiver } from './mail-receiver.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<MailReceiver>;

@Injectable()
export class MailReceiverService {

    private resourceUrl =  SERVER_API_URL + 'api/mail-receivers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/mail-receivers';

    constructor(private http: HttpClient) { }

    create(mailReceiver: MailReceiver): Observable<EntityResponseType> {
        const copy = this.convert(mailReceiver);
        return this.http.post<MailReceiver>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(mailReceiver: MailReceiver): Observable<EntityResponseType> {
        const copy = this.convert(mailReceiver);
        return this.http.put<MailReceiver>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<MailReceiver>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<MailReceiver[]>> {
        const options = createRequestOption(req);
        return this.http.get<MailReceiver[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MailReceiver[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<MailReceiver[]>> {
        const options = createRequestOption(req);
        return this.http.get<MailReceiver[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MailReceiver[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: MailReceiver = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<MailReceiver[]>): HttpResponse<MailReceiver[]> {
        const jsonResponse: MailReceiver[] = res.body;
        const body: MailReceiver[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to MailReceiver.
     */
    private convertItemFromServer(mailReceiver: MailReceiver): MailReceiver {
        const copy: MailReceiver = Object.assign({}, mailReceiver);
        return copy;
    }

    /**
     * Convert a MailReceiver to a JSON which can be sent to the server.
     */
    private convert(mailReceiver: MailReceiver): MailReceiver {
        const copy: MailReceiver = Object.assign({}, mailReceiver);
        return copy;
    }
}
