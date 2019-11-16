import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { AuthorityResource } from './authority-resource.model';
import { Role } from './role.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Role>;

@Injectable()
export class AuthorityResourceService {

    private roleUrl = SERVER_API_URL + 'api/roles';
    private authorityGroupUrl = SERVER_API_URL + 'api/authority-groups';

    constructor(private http: HttpClient) { }

    create(role: Role): Observable<EntityResponseType> {
        const copy = this.convert(role);
        return this.http.post<Role>(this.roleUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(authorityResource: Role): Observable<EntityResponseType> {
        const copy = this.convert(authorityResource);
        return this.http.put<Role>(this.roleUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(name: string): Observable<EntityResponseType> {
        return this.http.get<Role>(`${this.roleUrl}/${name}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Role[]>> {
        const options = createRequestOption(req);
        return this.http.get<Role[]>(this.roleUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Role[]>) => this.convertArrayResponse(res));
    }

    delete(name: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.roleUrl}/${name}`, { observe: 'response'});
    }

    queryResourceGroups(req?: any): Observable<HttpResponse<string[]>> {
        const options = createRequestOption(req);
        return this.http.get<string[]>(this.authorityGroupUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<string[]>) => this.convertStringArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Role = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Role[]>): HttpResponse<Role[]> {
        const jsonResponse: Role[] = res.body;
        const body: Role[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    private convertStringArrayResponse(res: HttpResponse<string[]>): HttpResponse<string[]> {
        const jsonResponse: string[] = res.body;
        const body: string[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertStringItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    private convertItemFromServer(role: Role): Role {
        const copy: Role = Object.assign({}, role);
        return copy;
    }

    private convertStringItemFromServer(group: string): string {
        const copy: string = group;
        return copy;
    }

    private convert(role: Role): Role {
        const copy: Role = Object.assign({}, role);
        return copy;
    }
}
