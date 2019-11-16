import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { TMSCustomFieldScreen } from './tms-custom-field-screen.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TMSCustomFieldScreen>;

@Injectable()
export class TMSCustomFieldScreenService {

    private resourceUrl =  SERVER_API_URL + 'api/tms-custom-field-screens';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/tms-custom-field-screens';

    constructor(private http: HttpClient) { }

    create(tMSCustomFieldScreen: TMSCustomFieldScreen): Observable<EntityResponseType> {
        const copy = this.convert(tMSCustomFieldScreen);
        return this.http.post<TMSCustomFieldScreen>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(tMSCustomFieldScreen: TMSCustomFieldScreen): Observable<EntityResponseType> {
        const copy = this.convert(tMSCustomFieldScreen);
        return this.http.put<TMSCustomFieldScreen>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TMSCustomFieldScreen>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    findByCustomFieldAndWorkflow(id?: number, workflowId?: any): Observable<EntityResponseType> {
        return this.http.get<TMSCustomFieldScreen>(`${this.resourceUrl}/${id}/${workflowId}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TMSCustomFieldScreen[]>> {
        const options = createRequestOption(req);
        return this.http.get<TMSCustomFieldScreen[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TMSCustomFieldScreen[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<TMSCustomFieldScreen[]>> {
        const options = createRequestOption(req);
        return this.http.get<TMSCustomFieldScreen[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TMSCustomFieldScreen[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        if (res.body === null) {
            const body: TMSCustomFieldScreen = undefined;
            return res.clone({body});
        }
        const body: TMSCustomFieldScreen = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TMSCustomFieldScreen[]>): HttpResponse<TMSCustomFieldScreen[]> {
        const jsonResponse: TMSCustomFieldScreen[] = res.body;
        const body: TMSCustomFieldScreen[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TMSCustomFieldScreen.
     */
    private convertItemFromServer(tMSCustomFieldScreen: TMSCustomFieldScreen): TMSCustomFieldScreen {
        const copy: TMSCustomFieldScreen = Object.assign({}, tMSCustomFieldScreen);
        return copy;
    }

    /**
     * Convert a TMSCustomFieldScreen to a JSON which can be sent to the server.
     */
    private convert(tMSCustomFieldScreen: TMSCustomFieldScreen): TMSCustomFieldScreen {
        const copy: TMSCustomFieldScreen = Object.assign({}, tMSCustomFieldScreen);
        copy.entityGridOp = JSON.stringify(tMSCustomFieldScreen.entityGridOp);
        copy.entityGridPm = JSON.stringify(tMSCustomFieldScreen.entityGridPm);
        return copy;
    }
}
