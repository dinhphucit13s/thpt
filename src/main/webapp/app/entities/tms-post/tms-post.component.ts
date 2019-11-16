import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TmsPost } from './tms-post.model';
import { TmsPostService } from './tms-post.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-tms-post',
    templateUrl: './tms-post.component.html'
})
export class TmsPostComponent implements OnInit, OnDestroy {
tmsPosts: TmsPost[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private tmsPostService: TmsPostService,
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
            this.tmsPostService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<TmsPost[]>) => this.tmsPosts = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.tmsPostService.query().subscribe(
            (res: HttpResponse<TmsPost[]>) => {
                this.tmsPosts = res.body;
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
        this.registerChangeInTmsPosts();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TmsPost) {
        return item.id;
    }
    registerChangeInTmsPosts() {
        this.eventSubscriber = this.eventManager.subscribe('tmsPostListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
