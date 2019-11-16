import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

@Injectable()
export class PasswordService {
    private resourceUrl =  SERVER_API_URL + 'api/account/change-password-user';
    constructor(private http: HttpClient) {}

    save(newPassword: string): Observable<any> {
        return this.http.post(SERVER_API_URL + 'api/account/change-password', newPassword);
    }

    savePassUser(newPassword?: any, user?: any): Observable<any> {
        return this.http.post(`${this.resourceUrl}?password=${newPassword}&user=${user}`, {observe: 'response'});
    }
}
