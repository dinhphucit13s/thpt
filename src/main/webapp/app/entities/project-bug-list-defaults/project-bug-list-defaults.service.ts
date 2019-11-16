import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {SERVER_API_URL} from '../../app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import {createRequestOption} from '../../shared';
import {ProjectBugListDefaults} from './project-bug-list-defaults.model';
import {BugListDefault} from '../bug-list-default';
import {ProjectUsers} from '../project-users/project-users.model';
import {ProjectBugListDefault} from '../project-bug-list-default/project-bug-list-default.model';

export type EntityResponseType = HttpResponse<ProjectBugListDefaults>;

@Injectable()
export class ProjectBugListDefaultsService {

    private resourceUrl =  SERVER_API_URL + 'api/project-bug-list-defaults';
    private resourceUrlGetListBug =  SERVER_API_URL + 'api/project-bug-list-defaults-by-projectId';
    private resourceSearchUrl =  SERVER_API_URL + 'api/_search/projects-bug-list-default';

    constructor(private http: HttpClient) { }

    /*conghk*/
    queryByProject(req?: any, proId?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrlGetListBug}?projectId=${proId}`, { params: options, observe: 'response' })
            .map((res: HttpResponse<any[]>) => this.convertArrayResponseProjectBug(res));
    }

    create(projectBugListDefaults: ProjectBugListDefaults[]): Observable<EntityResponseType> {
        return this.http.post<ProjectBugListDefaults>(this.resourceUrl, projectBugListDefaults, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    search(req?: any, searchValue?: any, proId?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);

        return this.http.get<any[]>(`${this.resourceSearchUrl}?searchValue=${searchValue}&proId=${proId}`, { params: options, observe: 'response' })
            .map((res: HttpResponse<any[]>) => this.convertArrayResponse(res, proId));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ProjectBugListDefaults>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertArrayResponseProjectBug(res: HttpResponse<ProjectBugListDefaults[]>): HttpResponse<ProjectBugListDefaults[]> {
        const jsonResponse: ProjectBugListDefaults[] = res.body;
        const body: ProjectBugListDefaults[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<BugListDefault[]>, proId: number): HttpResponse<ProjectBugListDefaults[]> {
        const jsonResponse: BugListDefault[] = res.body;
        const body: ProjectBugListDefaults[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            const projectBugListDefaults: ProjectBugListDefaults = {};
            //projectBugListDefaults.id = proId.toString() + '/' + jsonResponse[i].id;
            projectBugListDefaults.bugListDefaultDescription = jsonResponse[i].description;
            body.push(this.convertItemFromServer(projectBugListDefaults));
        }
        const xxx = res.clone({body});
        return res.clone({body});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ProjectBugListDefaults = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Projects.
     */
    private convertItemFromServer(projectBugListDefaults: ProjectBugListDefaults): ProjectBugListDefaults {
        const copy: ProjectBugListDefaults = Object.assign({}, projectBugListDefaults);
        return copy;
    }
}
