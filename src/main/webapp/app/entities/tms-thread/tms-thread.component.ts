import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { TmsThread } from './tms-thread.model';
import { TmsThreadService } from './tms-thread.service';
import { ITEMS_PER_PAGE, Principal } from '../../shared';
import {Projects, ProjectsService} from '../projects';
import {MatDialog} from '@angular/material';
import {TmsThreadDialogComponent} from './tms-thread-dialog.component';

@Component ({
    selector: 'jhi-question-and-answer',
    templateUrl: './tms-thread.component.html',
    styleUrls: ['./tms-thread.component.css']
})

export class TmsThreadComponent implements OnInit, OnDestroy {
    projects: Projects[];
    currentUserLogin: any;
    projectId: any;
    projectName: string;
    filter: any;
    eventSubscriber: Subscription;

    itemsPerPage: any;
    totalItems: any;
    queryCount: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    routeData: any;
    _page;

    listThread: any[];
    constructor(private projectsService: ProjectsService, private router: Router,
                private alertService: JhiAlertService,
                private threadService: TmsThreadService,
                private activatedRoute: ActivatedRoute,
                private eventManager: JhiEventManager) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            if (data.pagingParams !== undefined) {
                this.page = data.pagingParams.page;
                this.previousPage = data.pagingParams.page;
                this.reverse = data.pagingParams.ascending;
                this.predicate = data.pagingParams.predicate;
            } else {
                this.page = 1;
                this.previousPage = 1;
                this.reverse = true;
                this.predicate = 'id';
            }
        });
    }

    ngOnInit() {
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (!currentUser) {
            this.alertService.error('Invalid action');
            this.router.navigate(['/']);
            return;
        }
        this.currentUserLogin = currentUser.login;
        this.loadListProjects(this.currentUserLogin);
        this.registerChangeInTmsThreads();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    loadListProjects(userLogin: any) {
        this.projectsService.getListProjectByUserLogin(userLogin).subscribe(
            (res: HttpResponse<Projects[]>) => {
                this.projects = res.body;
                if (this.projects.length > 0) {
                    this.projects.forEach((project) => {
                        if (project.hasDoingTask !== undefined && project.hasDoingTask) {
                            this.projectId = project.id;
                            this.projectName = project.name;
                            return;
                        }
                    });
                    if (this.projectId === undefined) {
                        this.projectId = this.projects[0].id;
                        this.projectName = this.projects[0].name;
                    }
                    console.log(this.projectId);
                    this.loadListQuestionAndAnswer();
                }
            },
            (res: HttpErrorResponse) => {
                // this.onError(res.message);
            }
        );
        this.filter = 'all';

        this._page = +this.activatedRoute.snapshot.queryParams['page'];
        if (this._page >= 0) {
            this.page = this._page;
        }
    }

    changeFilter() {
        this.page = 1;
        this.loadListQuestionAndAnswer();
    }

    loadListQuestionAndAnswer() {
        this.threadService.loadListQuestionAndAnswer({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.projectId, this.filter).subscribe( (res) => {
            this.listThread = res.body;
            this.totalItems = res.headers.get('X-Total-Count');
            this.queryCount = this.totalItems;
            console.log(this.listThread);
        });
    }

    registerChangeInTmsThreads() {
        this.eventSubscriber = this.eventManager.subscribe('tmsThreadListModification', (response) => this.loadListQuestionAndAnswer());
    }


    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.loadListQuestionAndAnswer();
        }
    }

    updateViewsOfThread(threadId: any) {
        this.threadService.updateViewsOfThread(threadId).subscribe();
    }
}
