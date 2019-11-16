import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import {ProjectWorkflows, TaskWorkflowVM} from './project-workflows.model';
import { createRequestOption } from '../../shared';
import {TMSCustomField} from '../tms-custom-field/tms-custom-field.model';

export type EntityResponseType = HttpResponse<ProjectWorkflows>;

@Injectable()
export class ProjectWorkflowsService {

    private resourceUrl =  SERVER_API_URL + 'api/project-workflows';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/project-workflows';
    private resourceFindByTemplateUrl = SERVER_API_URL + 'api/project-workflows-by-template';
    private resourceFindDeployedProcess = SERVER_API_URL + 'api/project-workflows-processes';

    constructor(private http: HttpClient) { }

    create(projectWorkflows: ProjectWorkflows[]): Observable<EntityResponseType> {
        // const copy: ProjectWorkflows[] = new Array<ProjectWorkflows>();
        // for (const wf of projectWorkflows){
        //     wf.inputDTO = JSON.stringify(wf.inputDTO);
        //     copy.push(this.convert(wf));
        // }
        // const copy: ProjectWorkflows[] = new Array<ProjectWorkflows>();
        // const temp = JSON.stringify(projectWorkflows);
        //
        // const clone = JSON.parse(temp);
        // for (let i = 0; i < clone.length; i++) {
        //     clone[i].inputDTO = JSON.stringify(clone[i].inputDTO);
        //     copy.push(this.convert(clone[i]));
        // }
        const copy = this.convertItems(projectWorkflows);
        return this.http.post<ProjectWorkflows>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(projectWorkflows: ProjectWorkflows[]): Observable<EntityResponseType> {
        // const copy: ProjectWorkflows[] = new Array<ProjectWorkflows>();
        // for (const wf of projectWorkflows){
        //     wf.inputDTO = JSON.stringify(wf.inputDTO);
        //     copy.push(this.convert(wf));
        // }
        const copy = this.convertItems(projectWorkflows);
        return this.http.put<ProjectWorkflows>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ProjectWorkflows>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }
    findByTemplate(templateId: number):  Observable<HttpResponse<ProjectWorkflows[]>> {
        return this.http.get<ProjectWorkflows[]>(`${this.resourceFindByTemplateUrl}/${templateId}`, { observe: 'response'})
            .map((res: HttpResponse<ProjectWorkflows[]>) => this.convertArrayResponse(res));
    }

    findDeployedProcesses():  Observable<HttpResponse<TaskWorkflowVM[]>> {
        return this.http.get<TaskWorkflowVM[]>(this.resourceFindDeployedProcess, { observe: 'response'})
            .map((res: HttpResponse<TaskWorkflowVM[]>) => res);
    }

    // findByTemplate(templateId: number) {
    //     this.http.get<ProjectWorkflows[]>(`${this.resourceFindByTemplateUrl}/${templateId}`).toPromise().then((data) => {
    //         //this.promiseResult = data;
    //         //return this.convertArrayResponse(data)
    //         let test = <ProjectWorkflows[]>data;
    //         console.log('Promise resolved.');
    //         return test;
    //     });
    //     console.log('I will not wait until promise is resolved..');
    // }

    query(req?: any): Observable<HttpResponse<ProjectWorkflows[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectWorkflows[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectWorkflows[]>) => this.convertArrayResponse(res));
    }

    getWorkflowByProjectTemplateId(id?: any): Observable<HttpResponse<ProjectWorkflows[]>> {
        return this.http.get<ProjectWorkflows[]>(`${this.resourceUrl}/projectTemplate/${id}`, { observe: 'response' })
            .map((res: HttpResponse<ProjectWorkflows[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ProjectWorkflows[]>> {
        const options = createRequestOption(req);
        return this.http.get<ProjectWorkflows[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ProjectWorkflows[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ProjectWorkflows = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ProjectWorkflows[]>): HttpResponse<ProjectWorkflows[]> {
        const jsonResponse: ProjectWorkflows[] = res.body;
        const body: ProjectWorkflows[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    private convertItems(projectWorkflows: ProjectWorkflows[]): ProjectWorkflows[] {
        const copy: ProjectWorkflows[] = new Array<ProjectWorkflows>();
        for (const wf of projectWorkflows){
            wf.entityDTO = JSON.stringify(wf.entityDTO);
            wf.inputDTO = JSON.stringify(wf.inputDTO);
            wf.pmGridDTO = JSON.stringify(wf.pmGridDTO);
            wf.opGridDTO = JSON.stringify(wf.opGridDTO);
            if (wf.tmsCustomFields !== undefined) {
                for (const custom of wf.tmsCustomFields) {
                    custom.entityData = JSON.stringify(custom.entityData);
                    custom.entityGridInput = JSON.stringify(custom.entityGridInput);
                    custom.entityGridPm = JSON.stringify(custom.entityGridPm);
                    custom.entityGridOp = JSON.stringify(custom.entityGridOp);
                }
            }
            copy.push(this.convert(wf));
        }
        return copy;
    }

    /**
     * Convert a returned JSON object to ProjectWorkflows.
     */
    private convertItemFromServer(projectWorkflows: ProjectWorkflows): ProjectWorkflows {
        const copy: ProjectWorkflows = Object.assign({}, projectWorkflows);
        return copy;
    }

    /**
     * Convert a ProjectWorkflows to a JSON which can be sent to the server.
     */
    private convert(projectWorkflows: ProjectWorkflows): ProjectWorkflows {
        const copy: ProjectWorkflows = Object.assign({}, projectWorkflows);
        return copy;
    }
}
