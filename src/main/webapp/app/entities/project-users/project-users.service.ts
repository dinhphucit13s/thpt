import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ProjectUsers } from './project-users.model';
import { createRequestOption } from '../../shared';
import {Projects} from '../projects/projects.model';
import {BugListDefault} from '../bug-list-default/bug-list-default.model';

export type EntityResponseType = HttpResponse<ProjectUsers>;

@Injectable()
export class ProjectUsersService {

    private resourceUrl =  SERVER_API_URL + 'api/project-users';
    private resourceUrlList =  SERVER_API_URL + 'api/project-users-list';
    private resourceProListRole =  SERVER_API_URL + 'api/project-users-by-role';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/project-users';
    private resourceListProjects = SERVER_API_URL + 'api/projects-list';
    private resourceListProjectsUser = SERVER_API_URL + 'api/project-users-by-user';
    private resourceUrlAllocation =  SERVER_API_URL + 'api/project-users/allocation';

    constructor(private http: HttpClient) { }

    createList(projectUsers: ProjectUsers[]): Observable<EntityResponseType> {
        console.log('projectsUserService');
        console.log(projectUsers);
        return this.http.post<ProjectUsers>(this.resourceUrl, projectUsers, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    create(projectUsers: ProjectUsers): Observable<EntityResponseType> {
        const copy = this.convert(projectUsers);
        return this.http.post<ProjectUsers>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(projectUsers: ProjectUsers): Observable<EntityResponseType> {
        const copy = this.convert(projectUsers);
        return this.http.put<ProjectUsers>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ProjectUsers>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any, proId?: any): Observable<HttpResponse<ProjectUsers[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectUsers[]>(`${this.resourceUrlList}?query=${proId}`, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectUsers[]>) => this.convertArrayResponse(res));
    }

    queryRole(req?: any): Observable<HttpResponse<ProjectUsers[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectUsers[]>(this.resourceProListRole, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectUsers[]>) => this.convertArrayResponse(res));
    }

    getListProjectByUserLogin(userLogin?: any): Observable<HttpResponse<Projects[]>> {

        const options = createRequestOption(userLogin);
        return this.http.get<Projects[]>(`${this.resourceListProjects}?userLogin=${userLogin}`, { observe: 'response'})
            .map((res: HttpResponse<Projects[]>) => this.convertArrayResponse(res));
    }

    getListProjectUserByUserLogin(req?: any, userLogin?: any): Observable<HttpResponse<ProjectUsers[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectUsers[]>(`${this.resourceListProjectsUser}?userLogin=${userLogin}`, {params: options, observe: 'response'})
            .map((res: HttpResponse<ProjectUsers[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ProjectUsers[]>> {
        const options = createRequestOption(req);
        console.log(req);
        console.log(options);
        return this.http.get<ProjectUsers[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectUsers[]>) => this.convertArrayResponse(res));
    }

    import(formData: FormData): Observable<HttpResponse<ProjectUsers[]>> {
        return this.http.post<any>(`${this.resourceUrl}/import`, formData, { observe: 'response' })
            .map((res: HttpResponse<ProjectUsers[]>) => {
                return res;
            });
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ProjectUsers = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ProjectUsers[]>): HttpResponse<ProjectUsers[]> {
        const jsonResponse: ProjectUsers[] = res.body;
        const body: ProjectUsers[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ProjectUsers.
     */
    private convertItemFromServer(projectUsers: ProjectUsers): ProjectUsers {
        const copy: ProjectUsers = Object.assign({}, projectUsers);
        return copy;
    }

    /**
     * Convert a ProjectUsers to a JSON which can be sent to the server.
     */
    private convert(projectUsers: ProjectUsers): ProjectUsers {
        const copy: ProjectUsers = Object.assign({}, projectUsers);
        return copy;
    }
}
