import { Component, OnInit, OnDestroy } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import { TmsThread } from './tms-thread.model';
import { TmsThreadService } from './tms-thread.service';
import {TmsPost, TmsPostService} from '../tms-post';
import {ITEMS_PER_PAGE} from '../../shared';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
    selector: 'jhi-tms-thread-detail',
    templateUrl: './tms-thread-detail.component.html',
    styleUrls: ['./tms-thread-detail.component.css']
})
export class TmsThreadDetailComponent implements OnInit, OnDestroy {

    threadId: any;
    thread: any;
    currentUserLogin: any;
    itemsPerPage: any;
    totalItemsAnswer: any;
    queryCount: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    routeData: any;
    listAnswer: TmsPost[] = new Array();
    threadTitle: any;
    isEditTitle = false;

    mainPost: TmsPost;

    tmsThread: TmsThread;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private threadService: TmsThreadService,
        private postsService: TmsPostService,
        private router: Router,
        private route: ActivatedRoute,
        private alertService: JhiAlertService,
        private spinnerService: NgxSpinnerService
    ) {

        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.route.data.subscribe((data) => {
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
        this.threadId = this.route.snapshot.paramMap.get('id');
        this.subscription = this.route.params.subscribe((params) => {
            // this.load(params['id']);
            this.threadService.find(this.threadId).subscribe( (res) => {
                this.thread = res.body;
                this.mainPost = this.thread.posts[0];
                this.threadTitle = this.thread.title;
                console.log(this.thread);
                this.loadAnswerOfThread(this.mainPost.id);
            });
        });
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (!currentUser) {
            this.alertService.error('Invalid action');
            this.router.navigate(['/']);
            return;
        }
        this.currentUserLogin = currentUser.login.toLocaleLowerCase();
        console.log(this.threadId);

        this.registerChangeInTmsThreads();
        this.registerClosedInTmsThreads();
        this.registerCreatePostInTmsThreads();
    }

    loadAnswerOfThread(exceptId: any) {
        this.postsService.getAnswerOfThread({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.threadId, exceptId).subscribe( (res) => {
            this.listAnswer = (res.body);
            this.totalItemsAnswer = res.headers.get('X-Total-Count');
            this.queryCount = this.totalItemsAnswer;
        });
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.loadAnswerOfThread(this.mainPost.id);
        }
    }

    previousState() {
        window.history.back();
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'desc' : 'asc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    changeThreadToClose() {
        if (this.thread.createdBy.toLocaleLowerCase() === this.currentUserLogin) {
            this.threadService.changeThreadToClose(this.thread.id).subscribe( (res) => {
                this.thread.closed = true;
            });
        }
    }

    changeTitleOfThread() {
        this.threadService.changeTitleOfThread(this.thread.id, this.threadTitle).subscribe( (res) => {
            const theadResponse: any = res;
            this.thread.title = theadResponse.title;
            this.threadTitle = this.thread.title;
            this.isEditTitle = false;
        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTmsThreads() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tmsThreadListModification',
            (response) => this.loadAnswerOfThread(this.mainPost.id)
        );
    }
    registerClosedInTmsThreads() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tmsThreadClosedModification',
            (response) => this.changeThreadToClose()
        );
    }

    registerCreatePostInTmsThreads() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tmsPostInThreadListModification',
            (response) => {
                this.page = 1;
                this.thread.answers++;
                this.loadAnswerOfThread(this.mainPost.id);
            }
        );
    }
}
