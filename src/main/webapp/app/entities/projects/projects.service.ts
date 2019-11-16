import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Projects } from './projects.model';
import { createRequestOption } from '../../shared';
import {BugListDefault} from '../bug-list-default/bug-list-default.model';

export type EntityResponseType = HttpResponse<Projects>;

@Injectable()
export class ProjectsService {

    private resourceUrl =  SERVER_API_URL + 'api/projects';
    private resourceProjectsMonitoring =  SERVER_API_URL + 'api/projects-monitoring';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/projects';
    private resourceListProjects = SERVER_API_URL + 'api/projects-list';
    private resourceListProjectsForPO = SERVER_API_URL + 'api/projects-list-po';
    private resourceListBugDefaultsProjects = SERVER_API_URL + 'api/projects-create-bug-list-default';
    private resourceProjectsFeedback = SERVER_API_URL + 'api/feedbackProject';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(projects: Projects): Observable<EntityResponseType> {
        const copy = this.convert(projects);
        return this.http.post<Projects>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(projects: Projects): Observable<EntityResponseType> {
        const copy = this.convert(projects);
        return this.http.put<Projects>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Projects>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any, userLogin?: any): Observable<HttpResponse<Projects[]>> {
        const options = createRequestOption(req);
            return this.http.get<Projects[]>(this.resourceUrl, { params: options, observe: 'response' })
                .map((res: HttpResponse<Projects[]>) => this.convertArrayResponse(res));
    }

    queryProjectWithMonitoring(req?: any, userLogin?: any): Observable<HttpResponse<Projects[]>> {
        const options = createRequestOption(req);
        return this.http.get<Projects[]>(this.resourceProjectsMonitoring, { params: options, observe: 'response' })
            .map((res: HttpResponse<Projects[]>) => this.convertArrayResponse(res));
    }

    /*conghk*/
    queryByProject(req?: any, proId?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrl}/bug-list-defaults?projectId=${proId}`, { params: options, observe: 'response' })
            .map((res: HttpResponse<any[]>) => this.convertArrayResponse(res));
    }

    getListProjectByUserLogin(userLogin?: any): Observable<HttpResponse<Projects[]>> {

        const options = createRequestOption();
        return this.http.get<Projects[]>(this.resourceListProjects, { observe: 'response'})
            .map((res: HttpResponse<Projects[]>) => this.convertArrayResponse(res));
    }

    getListProjectForPOByUserLogin(userLogin?: any): Observable<HttpResponse<Projects[]>> {

        const options = createRequestOption();
        return this.http.get<Projects[]>(this.resourceListProjectsForPO, { observe: 'response'})
            .map((res: HttpResponse<Projects[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Projects[]>> {
        const options = createRequestOption(req);
        return this.http.get<Projects[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Projects[]>) => this.convertArrayResponse(res));
    }

    // conghk
    createListBugDefaults(projectsBugListDefaults: any): Observable<EntityResponseType> {
        console.log('projectsService');
        console.log(projectsBugListDefaults);
        return this.http.post<any>(this.resourceListBugDefaultsProjects, projectsBugListDefaults, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    findFeedbackProject(): Observable<EntityResponseType> {
        return this.http.get<Projects>(`${this.resourceProjectsFeedback}`, { observe: 'response'});
    }

    getListProjectBiddingTask(userLogin: any): Observable<EntityResponseType> {
        return this.http.get<Projects>(`${this.resourceUrl}/getProjectBidding?userLogin=${userLogin}`, { observe: 'response'});
    }

    // kimhq
    getProject(projectId?: any): Observable<EntityResponseType> {
        return this.http.get<Projects>(`${this.resourceUrl}/${projectId}`, {observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Projects = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Projects[]>): HttpResponse<Projects[]> {
        const jsonResponse: Projects[] = res.body;
        const body: Projects[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Projects.
     */
    private convertItemFromServer(projects: Projects): Projects {
        const copy: Projects = Object.assign({}, projects);
        copy.startTime = this.dateUtils
            .convertDateTimeFromServer(projects.startTime);
        copy.endTime = this.dateUtils
            .convertDateTimeFromServer(projects.endTime);
        return copy;
    }

    /**
     * Convert a Projects to a JSON which can be sent to the server.
     */
    private convert(projects: Projects): Projects {
        const copy: Projects = Object.assign({}, projects);

        copy.startTime = this.dateUtils.toDate(projects.startTime);

        copy.endTime = this.dateUtils.toDate(projects.endTime);
        return copy;
    }
}
