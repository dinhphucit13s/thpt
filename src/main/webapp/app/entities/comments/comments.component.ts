import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Comments } from './comments.model';
import { CommentsService } from './comments.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-comments',
    templateUrl: './comments.component.html'
})
export class CommentsComponent implements OnInit, OnDestroy {
comments: Comments[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private commentsService: CommentsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.commentsService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<Comments[]>) => this.comments = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.commentsService.query().subscribe(
            (res: HttpResponse<Comments[]>) => {
                this.comments = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        // this.registerChangeInComments();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Comments) {
        return item.id;
    }
    // registerChangeInComments() {
    //     this.eventSubscriber = this.eventManager.subscribe('commentsListModification', (response) => this.loadAll());
    // }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
