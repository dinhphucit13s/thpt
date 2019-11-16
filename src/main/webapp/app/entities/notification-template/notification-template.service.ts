import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { NotificationTemplate } from './notification-template.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<NotificationTemplate>;

@Injectable()
export class NotificationTemplateService {

    private resourceUrl =  SERVER_API_URL + 'api/notification-templates';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/notification-templates';

    constructor(private http: HttpClient) { }

    create(notificationTemplate: NotificationTemplate): Observable<EntityResponseType> {
        const copy = this.convert(notificationTemplate);
        return this.http.post<NotificationTemplate>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(notificationTemplate: NotificationTemplate): Observable<EntityResponseType> {
        const copy = this.convert(notificationTemplate);
        return this.http.put<NotificationTemplate>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<NotificationTemplate>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<NotificationTemplate[]>> {
        const options = createRequestOption(req);
        return this.http.get<NotificationTemplate[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<NotificationTemplate[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<NotificationTemplate[]>> {
        const options = createRequestOption(req);
        return this.http.get<NotificationTemplate[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<NotificationTemplate[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: NotificationTemplate = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<NotificationTemplate[]>): HttpResponse<NotificationTemplate[]> {
        const jsonResponse: NotificationTemplate[] = res.body;
        const body: NotificationTemplate[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to NotificationTemplate.
     */
    private convertItemFromServer(notificationTemplate: NotificationTemplate): NotificationTemplate {
        const copy: NotificationTemplate = Object.assign({}, notificationTemplate);
        return copy;
    }

    /**
     * Convert a NotificationTemplate to a JSON which can be sent to the server.
     */
    private convert(notificationTemplate: NotificationTemplate): NotificationTemplate {
        const copy: NotificationTemplate = Object.assign({}, notificationTemplate);
        return copy;
    }
}
