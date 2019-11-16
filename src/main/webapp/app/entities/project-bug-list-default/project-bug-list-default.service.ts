import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ProjectBugListDefault } from './project-bug-list-default.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ProjectBugListDefault>;

@Injectable()
export class ProjectBugListDefaultService {

    private resourceUrl =  SERVER_API_URL + 'api/project-bug-list-defaults';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/project-bug-list-defaults';

    constructor(private http: HttpClient) { }

    create(projectBugListDefault: ProjectBugListDefault): Observable<EntityResponseType> {
        const copy = this.convert(projectBugListDefault);
        return this.http.post<ProjectBugListDefault>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(projectBugListDefault: ProjectBugListDefault): Observable<EntityResponseType> {
        const copy = this.convert(projectBugListDefault);
        return this.http.put<ProjectBugListDefault>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ProjectBugListDefault>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ProjectBugListDefault[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectBugListDefault[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectBugListDefault[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ProjectBugListDefault[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectBugListDefault[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectBugListDefault[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ProjectBugListDefault = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ProjectBugListDefault[]>): HttpResponse<ProjectBugListDefault[]> {
        const jsonResponse: ProjectBugListDefault[] = res.body;
        const body: ProjectBugListDefault[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ProjectBugListDefault.
     */
    private convertItemFromServer(projectBugListDefault: ProjectBugListDefault): ProjectBugListDefault {
        const copy: ProjectBugListDefault = Object.assign({}, projectBugListDefault);
        return copy;
    }

    /**
     * Convert a ProjectBugListDefault to a JSON which can be sent to the server.
     */
    private convert(projectBugListDefault: ProjectBugListDefault): ProjectBugListDefault {
        const copy: ProjectBugListDefault = Object.assign({}, projectBugListDefault);
        return copy;
    }
}
