import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../app.constants';
import { JhiDateUtils } from 'ng-jhipster';
import {EntityResponseType} from '../entities/packages/packages.service';
import { createRequestOption } from '../shared';
import {PurchaseOrders} from '../entities/purchase-orders/purchase-orders.model';

@Injectable()
export class HomeService {

    private resourceUrl =  SERVER_API_URL + 'api/dashboard';
    private resourceDashboardReportUrl =  SERVER_API_URL + 'api/dashboard/reports';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    getSizePurchaseOrdersRelatingToProject(id?: any): Observable<HttpResponse<any>> {
        if (id !== undefined || id !== null) {
            return this.http.get<any>(`${this.resourceUrl}/projects/${id}/purchase-orders`, { observe: 'response' })
                .map((res: HttpResponse<any>) => {
                    return res;
                });
        }
    }

    getSizeTasksRelatingToProject(id?: any): Observable<HttpResponse<any>> {
        if (id !== undefined || id !== null) {
            return this.http.get<any>(`${this.resourceUrl}/projects/${id}/tasks`, { observe: 'response' })
                .map((res: HttpResponse<any>) => {
                    return res;
                });
        }
    }

    getTasksByFilter(filter: any, projectId: any): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrl}/tasks/${projectId}/${filter}`, { observe: 'response' });
    }

    getSizePackageRelatingToProject(id?: any): Observable<HttpResponse<any>> {
        if (id !== undefined || id !== null) {
            return this.http.get<any>(`${this.resourceUrl}/projects/${id}/packages`, { observe: 'response' })
                .map((res: HttpResponse<any>) => {
                    return res;
                });
        }
    }

    getSizeUserRelatingToProject(id?: any): Observable<HttpResponse<any>> {
        if (id !== undefined || id !== null) {
            return this.http.get<any>(`${this.resourceUrl}/projects/${id}/users`, { observe: 'response' })
                .map((res: HttpResponse<any>) => {
                    return res;
                });
        }
    }

    loadLateTaskColumn(id: number): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/fields/${id}/tasks-late`, { observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }
    loadLateTaskData(req?: any, id?: number): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/projects/${id}/tasks-late`, {params: options, observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    loadLatePackageColumn(id: number): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/fields/${id}/packages-late`, { observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }
    loadLatePackageData(req?: any, id?: number): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/projects/${id}/packages-late`, { params: options, observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    loadUnAssignTaskColumn(id: number): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/fields/${id}/tasks-unassign`, { observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }
    loadUnAssignTaskData(req?: any, id?: number): Observable<EntityResponseType> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceUrl}/projects/${id}/tasks-unassign`, { params: options, observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }
}
