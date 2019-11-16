import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Mail } from './mail.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Mail>;

@Injectable()
export class MailService {

    private resourceUrl =  SERVER_API_URL + 'api/mail';
    private resourceUrlStatus =  SERVER_API_URL + 'api/mail-status';
    private resourceSendMail =  SERVER_API_URL + 'api/mail-send';
    private resourceReceiverMail =  SERVER_API_URL + 'api/mail-receiver';
    private resourceAttachment =  SERVER_API_URL + 'api/mail/attachment';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/mail';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(mail: Mail, attachments: any): Observable<EntityResponseType> {
        const copy = this.convert(mail);
        const formData: FormData = new FormData();
        for (let i = 0; i < attachments.length; i ++) {
            formData.append('attachments', attachments[i]);
        }
        formData.append('mail', new Blob([JSON.stringify(copy)], {
            type: 'application/json'
        }));
        return this.http.post<Mail>(this.resourceUrl, formData, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(mail: Mail, attachments: any): Observable<EntityResponseType> {
        const copy = this.convert(mail);
        const formData: FormData = new FormData();
        for (let i = 0; i < attachments.length; i ++) {
            formData.append('attachments', attachments[i]);
        }

        formData.append('mail', new Blob([JSON.stringify(copy)], {
            type: 'application/json'
        }));
        return this.http.put<Mail>(this.resourceUrl, formData, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number, userLogin?: any): Observable<EntityResponseType> {
        return this.http.get<Mail>(`${this.resourceUrl}/${id}/${userLogin}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    findAttachment(id: number): Observable<EntityResponseType> {
        return this.http.get<any>(`${this.resourceAttachment}?id=${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => {
                return res;
            });
    }

    query(req?: any): Observable<HttpResponse<Mail[]>> {
        const options = createRequestOption(req);
        return this.http.get<Mail[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Mail[]>) => this.convertArrayResponse(res));
    }

    querySendMail(req?: any, userLogin?: any): Observable<HttpResponse<Mail[]>> {
        const options = createRequestOption(req);
        return this.http.get<Mail[]>(`${this.resourceSendMail}?userLogin=${userLogin}`, { params: options, observe: 'response' })
            .map((res: HttpResponse<Mail[]>) => this.convertArrayResponse(res));
    }

    queryReceiverMail(req?: any, userLogin?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceReceiverMail}?userLogin=${userLogin}`, { params: options, observe: 'response' })
            .map((res: HttpResponse<any>) => {
                return res;
            });
    }

    countMailUnseen(userLogin?: any) {
        return this.http.get(`${this.resourceReceiverMail}/${userLogin}/count-mailUnseen`, { observe: 'response'});
    }

    countMailInboxOfUserLogin(userLogin?: any) {
        return this.http.get(`${this.resourceReceiverMail}/count-inbox?userLogin=${userLogin}`, { observe: 'response'});
    }

    queryReceiverMailUnread(req?: any, userLogin?: any): Observable<HttpResponse<any>> {
        const options = createRequestOption(req);
        return this.http.get<any>(`${this.resourceReceiverMail}/unread?userLogin=${userLogin}`, { params: options, observe: 'response' })
            .map((res: HttpResponse<any>) => {
                return res;
            });
    }

    getAttachmentByMailId(mailId: any): Observable<HttpResponse<any[]>> {
        return this.http.get<any[]>(`${this.resourceUrl}/attachment?mailId=${mailId}`, { observe: 'response'});
    }

    updateStatusMail(id: number, userName): Observable<HttpResponse<any>> {
        return this.http.get<any>(`${this.resourceUrlStatus}?id=${id}&userLogin=${userName}`, { observe: 'response'});
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    deleteMail(dest: any, id: number, userName: any): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${dest}/${id}/${userName}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Mail[]>> {
        const options = createRequestOption(req);
        return this.http.get<Mail[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Mail[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Mail = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Mail[]>): HttpResponse<Mail[]> {
        const jsonResponse: Mail[] = res.body;
        const body: Mail[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Mail.
     */
    private convertItemFromServer(mail: Mail): Mail {
        const copy: Mail = Object.assign({}, mail);
        copy.attachments.forEach((attach) => {
            attach.diskFile = encodeURIComponent(attach.diskFile);
        });
        copy.startTime = this.dateUtils
            .convertDateTimeFromServer(mail.startTime);
        copy.endTime = this.dateUtils
            .convertDateTimeFromServer(mail.endTime);
        return copy;
    }

    /**
     * Convert a Mail to a JSON which can be sent to the server.
     */
    private convert(mail: Mail): Mail {
        const copy: Mail = Object.assign({}, mail);

        copy.startTime = this.dateUtils.toDate(mail.startTime);

        copy.endTime = this.dateUtils.toDate(mail.endTime);
        return copy;
    }
}
