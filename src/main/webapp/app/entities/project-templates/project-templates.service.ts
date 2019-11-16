import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ProjectTemplates } from './project-templates.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ProjectTemplates>;

@Injectable()
export class ProjectTemplatesService {

    private resourceUrl =  SERVER_API_URL + 'api/project-templates';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/project-templates';

    constructor(private http: HttpClient) { }

    create(projectTemplates: ProjectTemplates): Observable<EntityResponseType> {
        const copy = this.convert(projectTemplates);
        return this.http.post<ProjectTemplates>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(projectTemplates: ProjectTemplates): Observable<EntityResponseType> {
        const copy = this.convert(projectTemplates);
        return this.http.put<ProjectTemplates>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ProjectTemplates>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ProjectTemplates[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectTemplates[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectTemplates[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    exportTemplate(templateId) {
        //return this.http.get<any>(`${this.resourceUrl}/export?templateId=${templateId}`, { observe: 'response'});
        return this.http.get<any>(`${this.resourceUrl}/export?templateId=${templateId}`, { observe: 'response' })
            .map((res: HttpResponse<any>) => {
                return res;
            });
    }

    search(req?: any): Observable<HttpResponse<ProjectTemplates[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectTemplates[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectTemplates[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ProjectTemplates = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ProjectTemplates[]>): HttpResponse<ProjectTemplates[]> {
        const jsonResponse: ProjectTemplates[] = res.body;
        const body: ProjectTemplates[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ProjectTemplates.
     */
    private convertItemFromServer(projectTemplates: ProjectTemplates): ProjectTemplates {
        const copy: ProjectTemplates = Object.assign({}, projectTemplates);
        return copy;
    }

    /**
     * Convert a ProjectTemplates to a JSON which can be sent to the server.
     */
    private convert(projectTemplates: ProjectTemplates): ProjectTemplates {
        const copy: ProjectTemplates = Object.assign({}, projectTemplates);
        return copy;
    }
}
