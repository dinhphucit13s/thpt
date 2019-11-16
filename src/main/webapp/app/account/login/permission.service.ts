import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Permission } from './permission.model';

export type EntityResponseType = HttpResponse<Permission>;

@Injectable()
export class PermissionService {
    private permissionUrl = SERVER_API_URL + 'api/permission';

    constructor(private http: HttpClient) { }

    findPermission(): Observable<EntityResponseType> {
        return this.http.get<Permission>(`${this.permissionUrl}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Permission = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertItemFromServer(permission: Permission): Permission {
        const copy: Permission = Object.assign({}, permission);
        return copy;
    }
}