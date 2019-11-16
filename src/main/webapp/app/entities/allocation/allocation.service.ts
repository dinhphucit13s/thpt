import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';
import { MembersManagement } from '../members-management/members-management.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<any>;

@Injectable()
export class AllocationService {
    private resourceUrl =  SERVER_API_URL + 'api/project-users/allocation';
    private resourceUrlPackages =  SERVER_API_URL + 'api/packages/allocation';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/project-users/allocation';
    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    query(req?: any, projectId?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrl}?id=${projectId}`, {params: options, observe: 'response'})
            .map((res: HttpResponse<any[]>) => {
                return res;
            });
    }
    queryDetail(req?: any, projectUserId?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(`${this.resourceUrlPackages}?id=${projectUserId}`, {params: options, observe: 'response'})
            .map((res: HttpResponse<any[]>) => {
                return res;
            });
    }
    search(req?: any): Observable<HttpResponse<any[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<any[]>) => {
                return res;
            });
    }
    private convertArrayResponse(res: HttpResponse<any[]>): HttpResponse<any[]> {
        const jsonResponse: any[] = res.body;
        const body: any[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }
    private convertItemFromServer(membersManagement: any): any {
        const copy: any = Object.assign({}, membersManagement);
        return copy;
    }
    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: MembersManagement = this.convertItemFromServer(res.body);
        return res.clone({body});
    }
}
