import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Packages } from './packages.model';
import { createRequestOption } from '../../shared';
import {FieldConfig} from '../../shared/dynamic-forms/field.interface';
import {Tasks} from '../tasks';

export type EntityResponseType = HttpResponse<Packages>;

@Injectable()
export class PackagesService {

    private resourceUrl =  SERVER_API_URL + 'api/packages';
    private resourceBiddingUrl =  SERVER_API_URL + 'api/packages-bidding';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/packages';
    private resourceSearchPackageUrl = SERVER_API_URL + 'api/packages-search';
    private resourceDashboardUrl = SERVER_API_URL + 'api/dashboard';

    /*URL Api Clone Package
    PhuVD3*/
    private resourceCloneUrl =  SERVER_API_URL + 'api/packages-clone';
    private resourceCloneAllUrl =  SERVER_API_URL + 'api/packages-all';
    private resourceCloneOpenUrl =  SERVER_API_URL + 'api/packages-open';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(packages: Packages): Observable<EntityResponseType> {
        const copy = this.convert(packages);
        return this.http.post<Packages>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    /*Api Clone Package
    PhuVD3*/
    createCloneTasksAndPackage(packages: Packages): Observable<EntityResponseType> {
        const copy = this.convert(packages);
        return this.http.post<Packages>(this.resourceCloneUrl, copy , { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    /*Api All Tasks
    PhuVD3*/
    cloneAllTask(packageId: number, purchaseOrder: number, packagesLocal: any): Observable<HttpResponse<any>> {
        return this.http.put<any>(`${this.resourceCloneAllUrl}/${packageId}/${purchaseOrder}/${packagesLocal}`, { observe: 'response'});
    }
    /*Api Open Tasks
    PhuVD3*/
    cloneOpenTask(packageId: number, purchaseOrder: number, packagesLocal: any): Observable<HttpResponse<any>> {
        return this.http.put<any>(`${this.resourceCloneOpenUrl}/${packageId}/${purchaseOrder}/${packagesLocal}`, { observe: 'response'});
    }

    update(packages: Packages): Observable<EntityResponseType> {
        const copy = this.convert(packages);
        return this.http.put<Packages>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Packages>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any, puoId?: any): Observable<HttpResponse<Packages[]>> {
        const options = createRequestOption(req);
        if (puoId !== null && puoId !== undefined) {
            return this.http.get<Packages[]>(`${this.resourceUrl}?purchaseOrderId=${puoId}`, { params: options, observe: 'response' })
                .map((res: HttpResponse<Packages[]>) => this.convertArrayResponse(res));
        } else {
            return this.http.get<Packages[]>(this.resourceUrl, { params: options, observe: 'response' })
                .map((res: HttpResponse<Packages[]>) => this.convertArrayResponse(res));
        }
    }
    getPackages(req?: any, puoId?: any): Observable<HttpResponse<Packages[]>> {
        const options = createRequestOption(req);
            return this.http.get<Packages[]>(`${this.resourceUrl}?purchaseOrderId=${puoId}`, { params: options, observe: 'response' })
                .map((res: HttpResponse<Packages[]>) => this.convertArrayResponse(res));
    }

    getListPackageBiddingTask(poId: number): Observable<HttpResponse<Packages[]>> {
        return this.http.get<Packages[]>(`${this.resourceBiddingUrl}?purchaseOrderId=${poId}`, {observe: 'response' });
    }

    loadPackageColumn(id: number): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/fields/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    loadPackageDynamicColumn(id: number): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceUrl}/dynamic-fields/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    import(formData: FormData): Observable<EntityResponseType> {
        return this.http.post<any>(`${this.resourceUrl}/import`, formData, { observe: 'response' })
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    search(req?: any): Observable<HttpResponse<Packages[]>> {
        const options = createRequestOption(req);
        return this.http.get<Packages[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Packages[]>) => this.convertArrayResponse(res));
    }

    searchPackages(req?: any, packName?: any): Observable<HttpResponse<Packages[]>> {
        const options = createRequestOption(req);
        return this.http.get<Packages[]>(`${this.resourceSearchPackageUrl}?packName=${packName}`, { params: options, observe: 'response' })
            .map((res: HttpResponse<Packages[]>) => this.convertArrayResponse(res));
    }

    delivery(id: number): Observable<HttpResponse<any>> {
        return this.http.put<any>(`${this.resourceUrl}/${id}/delivery`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Packages = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Packages[]>): HttpResponse<Packages[]> {
        const jsonResponse: Packages[] = res.body;
        const body: Packages[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Packages.
     */
    private convertItemFromServer(packages: Packages): Packages {
        const copy: Packages = Object.assign({}, packages);
        copy.estimateDelivery = this.dateUtils
            .convertDateTimeFromServer(packages.estimateDelivery);
        copy.startTime = this.dateUtils
            .convertDateTimeFromServer(packages.startTime);
        copy.endTime = this.dateUtils
            .convertDateTimeFromServer(packages.endTime);
        return copy;
    }

    /**
     * Convert a Packages to a JSON which can be sent to the server.
     */
    private convert(packages: Packages): Packages {
        const copy: Packages = Object.assign({}, packages);

        copy.estimateDelivery = this.dateUtils.toDate(packages.estimateDelivery);

        copy.startTime = this.dateUtils.toDate(packages.startTime);

        copy.endTime = this.dateUtils.toDate(packages.endTime);
        return copy;
    }

    private covertFieldConfig(fieldConfig: any): FieldConfig {
        const copy: FieldConfig = new FieldConfig();
        fieldConfig.tmsCustomFieldScreenDTOs.forEach((field) => {

        });
        return null;
    }
}
