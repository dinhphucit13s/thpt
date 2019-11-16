import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { TMSCustomField } from './tms-custom-field.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TMSCustomField>;

@Injectable()
export class TMSCustomFieldService {

    private resourceUrl =  SERVER_API_URL + 'api/tms-custom-fields';
    private resourceSearchCustomField = SERVER_API_URL + 'api/tms-custom-fields-search';
    private resourceCheckExistsCustomField = SERVER_API_URL + 'api/tms-custom-fields-name-exists';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/tms-custom-fields';

    constructor(private http: HttpClient) { }

    create(tMSCustomField: TMSCustomField): Observable<EntityResponseType> {
        const copy = this.convertItemFromServerString(tMSCustomField);
        return this.http.post<TMSCustomField>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(tMSCustomField: TMSCustomField): Observable<EntityResponseType> {
        const copy = this.convertItemFromServerString(tMSCustomField);
        return this.http.put<TMSCustomField>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TMSCustomField>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TMSCustomField[]>> {
        const options = createRequestOption(req);
        return this.http.get<TMSCustomField[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TMSCustomField[]>) => this.convertArrayResponse(res));
    }

    searchCustomField(query?: any): Observable<HttpResponse<TMSCustomField[]>> {
        return this.http.get<TMSCustomField[]>(`${this.resourceSearchCustomField}?query=${query}`, { observe: 'response' })
            .map((res: HttpResponse<TMSCustomField[]>) => this.convertArrayResponse(res));
    }

    checkCustomFieldName(query?: any): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceCheckExistsCustomField}?query=${query}`, { observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<TMSCustomField[]>> {
        const options = createRequestOption(req);
        return this.http.get<TMSCustomField[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TMSCustomField[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TMSCustomField = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TMSCustomField[]>): HttpResponse<TMSCustomField[]> {
        const jsonResponse: TMSCustomField[] = res.body;
        const body: TMSCustomField[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TMSCustomField.
     */
    private convertItemFromServer(tMSCustomField: TMSCustomField): TMSCustomField {
        const copy: TMSCustomField = Object.assign({}, tMSCustomField);
        return copy;
    }

    private convertItemFromServerString(tMSCustomField: TMSCustomField): TMSCustomField {
        const copy: TMSCustomField = tMSCustomField;
        copy.entityData = JSON.stringify(tMSCustomField.entityData);
        return copy;
    }

    /**
     * Convert a TMSCustomField to a JSON which can be sent to the server.
     */
    private convert(tMSCustomField: TMSCustomField): TMSCustomField {
        const copy: TMSCustomField = Object.assign({}, tMSCustomField);
        return copy;
    }
}
