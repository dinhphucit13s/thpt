import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Bugs } from './bugs.model';
import { createRequestOption } from '../../shared';
import {Tasks} from '../tasks/tasks.model';

export type EntityResponseType = HttpResponse<Bugs>;

@Injectable()
export class BugsService {

    private resourceUrl =  SERVER_API_URL + 'api/bugs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/bugs';
    private resourceCountBugByUserAssign = SERVER_API_URL + 'api/bugs-count-bug-user-assign';
    private resourceCountBugByUserAssignList = SERVER_API_URL + 'api/bugs-count-List-user-assign';
    private resourceListBug =  SERVER_API_URL + 'api/bugs-list-by-Task';

    constructor(private http: HttpClient) { }

    create(bugs: Bugs): Observable<EntityResponseType> {
        const copy = this.convert(bugs);
        return this.http.post<Bugs>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(bugs: Bugs): Observable<EntityResponseType> {
        const copy = this.convert(bugs);
        return this.http.put<Bugs>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Bugs>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Bugs[]>> {
        const options = createRequestOption(req);
        return this.http.get<Bugs[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Bugs[]>) => this.convertArrayResponse(res));
    }

    queryListBugByTaskId(req?: any, taskId?: any): Observable<HttpResponse<Bugs[]>> {
        const options = createRequestOption(req);
        return this.http.get<Bugs[]>(`${this.resourceListBug}?taskId=${taskId}`, { params: options, observe: 'response' })
            .map((res: HttpResponse<Bugs[]>) => this.convertArrayResponse(res));
    }
    queryCountBugByUserAssign(taskId?: any, rowRV?: any): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceCountBugByUserAssign}?taskId=${taskId}&rowRV=${rowRV}`, {observe: 'response' })
            .map((res: HttpResponse<any>) => {
                return res;
            });
    }

    queryCountBugByUserAssignList(req?: any, tasksDTOs?: Tasks[], userLogin?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.post<any>(this.resourceCountBugByUserAssignList, tasksDTOs, {params: options, observe: 'response' })
            .map((res: HttpResponse<any>) => {
                return res;
            });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Bugs[]>> {
        const options = createRequestOption(req);
        return this.http.get<Bugs[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Bugs[]>) => this.convertArrayResponse(res));
    }

    findBugsByTasksId(id: number): Observable<HttpResponse<Bugs[]>> {
        return this.http.get<Bugs[]>(`${this.resourceUrl}/tasks/${id}`, { observe: 'response'});
    }

    getAttachmentByBugId(bugId: any): Observable<HttpResponse<any[]>> {
        console.log(bugId);
        return this.http.get<any[]>(`${this.resourceUrl}/attachment?bugId=${bugId}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Bugs = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Bugs[]>): HttpResponse<Bugs[]> {
        const jsonResponse: Bugs[] = res.body;
        const body: Bugs[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Bugs.
     */
    private convertItemFromServer(bugs: Bugs): Bugs {
        const copy: Bugs = Object.assign({}, bugs);
        return copy;
    }

    /**
     * Convert a Bugs to a JSON which can be sent to the server.
     */
    private convert(bugs: Bugs): Bugs {
        const copy: Bugs = Object.assign({}, bugs);
        return copy;
    }
}
