import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { BugListDefault } from './bug-list-default.model';
import { createRequestOption } from '../../shared';
import {ProjectUsers} from '../project-users/project-users.model';
import {User} from '../../shared/user/user.model';

export type EntityResponseType = HttpResponse<BugListDefault>;

@Injectable()
export class BugListDefaultService {

    private resourceUrl =  SERVER_API_URL + 'api/bug-list-defaults';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/bug-list-defaults';

    constructor(private http: HttpClient) { }

    create(bugListDefault: BugListDefault): Observable<EntityResponseType> {
        const copy = this.convert(bugListDefault);
        return this.http.post<BugListDefault>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(bugListDefault: BugListDefault): Observable<EntityResponseType> {
        const copy = this.convert(bugListDefault);
        return this.http.put<BugListDefault>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<BugListDefault>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<BugListDefault[]>> {
        const options = createRequestOption(req);
        return this.http.get<BugListDefault[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BugListDefault[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<BugListDefault[]>> {
        const options = createRequestOption(req);
        return this.http.get<BugListDefault[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<BugListDefault[]>) => this.convertArrayResponse(res));
    }

    loadBugListDefaultOfProject(idProject: any): Observable<HttpResponse<BugListDefault[]>> {
        return this.http.get<BugListDefault[]>(this.resourceUrl + '/projects?projectId=' + idProject, { observe: 'response' })
            .map((res: HttpResponse<BugListDefault[]>) => this.convertArrayResponse(res));
    }

    findBugListDefaultsUnexistInProject(projectId: any): Observable<HttpResponse<BugListDefault[]>> {
        return this.http.get<User[]>(`${this.resourceUrl}-unExist?projectId=${projectId}`, { observe: 'response' });
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: BugListDefault = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<BugListDefault[]>): HttpResponse<BugListDefault[]> {
        const jsonResponse: BugListDefault[] = res.body;
        const body: BugListDefault[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to BugListDefault.
     */
    private convertItemFromServer(bugListDefault: BugListDefault): BugListDefault {
        const copy: BugListDefault = Object.assign({}, bugListDefault);
        return copy;
    }

    /**
     * Convert a BugListDefault to a JSON which can be sent to the server.
     */
    private convert(bugListDefault: BugListDefault): BugListDefault {
        const copy: BugListDefault = Object.assign({}, bugListDefault);
        return copy;
    }
}
