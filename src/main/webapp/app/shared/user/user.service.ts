import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SERVER_API_URL } from '../../app.constants';
import { User } from './user.model';
import { createRequestOption } from '../model/request-util';
import { JhiDateUtils } from 'ng-jhipster';
import {Projects} from '../../entities/projects/projects.model';
import {ProjectUsers} from '../../entities/project-users';
export type EntityResponseType = HttpResponse<User>;

@Injectable()
export class UserService {
    private resourceUrl = SERVER_API_URL + 'api/users';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/users';
    private resourceUrlForPU = SERVER_API_URL + 'api/users-for-project-user';
    private resourceUrlForAllUsers = SERVER_API_URL + 'api/all-users';
    private resourceSearch = SERVER_API_URL + 'api/users-search';
    private resourceSearchMail = SERVER_API_URL + 'api/users-search-mail';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(user: User): Observable<HttpResponse<User>> {
        const copy = this.convert(user);
        return this.http.post<User>(this.resourceUrl, copy, { observe: 'response' });
    }

    update(user: User): Observable<HttpResponse<User>> {
        const copy = this.convert(user);
        return this.http.put<User>(this.resourceUrl, copy, { observe: 'response' });
    }

    find(login: string): Observable<HttpResponse<User>> {
        return this.http.get<User>(`${this.resourceUrl}/${login}`, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<User[]>> {
        const options = createRequestOption(req);
        return this.http.get<User[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<User[]>) => this.convertArrayResponse(res));
    }
    search(req?: any): Observable<HttpResponse<User[]>> {
        const options = createRequestOption(req);
        return this.http.get<User[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<User[]>) => this.convertArrayResponse(res));
    }
    querySearch(req?: any): Observable<HttpResponse<User[]>> {
        const options = createRequestOption(req);
        return this.http.get<User[]>(this.resourceSearch, { params: options, observe: 'response' })
            .map((res: HttpResponse<User[]>) => this.convertArrayResponse(res));
    }
    querySearchMail(query?: any): Observable<HttpResponse<User[]>> {
        return this.http.get<User[]>(`${this.resourceSearchMail}?query=${query}`, { observe: 'response' })
            .map((res: HttpResponse<User[]>) => this.convertArrayResponse(res));
    }
    getAllUsers(): Observable<HttpResponse<User[]>> {
        return this.http.get<User[]>(`${this.resourceUrlForAllUsers}`, { observe: 'response' })
            .map((res: HttpResponse<User[]>) => this.convertArrayResponse(res));
    }

    queryforPojectUsers(req?: any): Observable<HttpResponse<User[]>> {
        const options = createRequestOption(req);
        return this.http.get<User[]>(this.resourceUrlForPU, { params: options, observe: 'response' });
    }

    delete(login: string): Observable<HttpResponse<any>> {
        return this.http.delete(`${this.resourceUrl}/${login}`, { observe: 'response' });
    }

    import(formData: FormData): Observable<EntityResponseType> {
        return this.http.post<any>(`${this.resourceUrl}/import`, formData, { observe: 'response' })
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    getUserActivatedByProject(projectId: any): Observable<HttpResponse<ProjectUsers[]>> {
        return this.http.get<ProjectUsers[]>(`${this.resourceUrl}/activated/${projectId}`, { observe: 'response' });
    }

    authorities(): Observable<string[]> {
        return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: User = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<User[]>): HttpResponse<User[]> {
        const jsonResponse: User[] = res.body;
        const body: User[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to UserProfile.
     */
    private convertItemFromServer(user: User): User {
        const copy: User = Object.assign({}, user);
        copy.dob = this.dateUtils
            .convertLocalDateFromServer(user.dob);
        return copy;
    }

    /**
     * Convert a UserProfile to a JSON which can be sent to the server.
     */
    private convert(user: User): User {
        const copy: User = Object.assign({}, user);
        copy.dob = this.formatDate(user.dob);
        return copy;
    }

    private formatDate(date) {
        const d = new Date(date);
        let month = '' + (d.getMonth() + 1);
        let day = '' + d.getDate();
        const year = d.getFullYear();
        if (month.length < 2) {
            month = '0' + month;
        }
        if (day.length < 2) {
            day = '0' + day;
        }
        return [year, month, day].join('-');
    }
}
