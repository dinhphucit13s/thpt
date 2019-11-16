import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TmsPost } from './tms-post.model';
import { TmsPostService } from './tms-post.service';

@Component({
    selector: 'jhi-tms-post-detail',
    templateUrl: './tms-post-detail.component.html'
})
export class TmsPostDetailComponent implements OnInit, OnDestroy {

    tmsPost: TmsPost;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private tmsPostService: TmsPostService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTmsPosts();
    }

    load(id) {
        this.tmsPostService.find(id)
            .subscribe((tmsPostResponse: HttpResponse<TmsPost>) => {
                this.tmsPost = tmsPostResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTmsPosts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tmsPostListModification',
            (response) => this.load(this.tmsPost.id)
        );
    }
}
