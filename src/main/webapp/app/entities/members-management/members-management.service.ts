import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';
import { MembersManagement } from './members-management.model';
import { createRequestOption } from '../../shared';
import {Projects} from '../projects/projects.model';
import {ProjectUsers} from '../project-users/project-users.model';
import {BusinessLine} from '../business-line/business-line.model';

export type EntityResponseType = HttpResponse<MembersManagement>;

@Injectable()
export class MembersManagementService {
    private resourceUrl =  SERVER_API_URL + 'api/users-online';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/users-online';
    private resourceFine = SERVER_API_URL + 'api/users';
    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    query(req?: any): Observable<HttpResponse<MembersManagement[]>> {
        const options = createRequestOption(req);
        return this.http.get<MembersManagement[]>(`${this.resourceUrl}`, {params: options, observe: 'response'})
            .map((res: HttpResponse<MembersManagement[]>) => this.convertArrayResponse(res));
    }

    findByCondition(req?: any): Observable<HttpResponse<MembersManagement[]>> {
        const options = createRequestOption(req);
        return this.http.get<MembersManagement[]>(`${this.resourceFine}/condition`, {params: options, observe: 'response'})
            .map((res: HttpResponse<MembersManagement[]>) => this.convertArrayResponse(res));
    }
    find(login?: number): Observable<EntityResponseType> {
        return this.http.get<MembersManagement>(`${this.resourceFine}/${login}/member`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }
    search(req?: any): Observable<HttpResponse<MembersManagement[]>> {
        const options = createRequestOption(req);
        return this.http.get<MembersManagement[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MembersManagement[]>) => this.convertArrayResponse(res));
    }
    private convertArrayResponse(res: HttpResponse<MembersManagement[]>): HttpResponse<MembersManagement[]> {
        const jsonResponse: MembersManagement[] = res.body;
        const body: MembersManagement[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }
    private convertItemFromServer(membersManagement: MembersManagement): MembersManagement {
        const copy: MembersManagement = Object.assign({}, membersManagement);
        return copy;
    }
    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: MembersManagement = this.convertItemFromServer(res.body);
        return res.clone({body});
    }
}
