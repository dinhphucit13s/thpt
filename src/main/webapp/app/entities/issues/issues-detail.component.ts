import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Issues } from './issues.model';
import { IssuesService } from './issues.service';

@Component({
    selector: 'jhi-issues-detail',
    templateUrl: './issues-detail.component.html'
})
export class IssuesDetailComponent implements OnInit, OnDestroy {

    issues: Issues;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private issuesService: IssuesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInIssues();
    }

    load(id) {
        this.issuesService.find(id)
            .subscribe((issuesResponse: HttpResponse<Issues>) => {
                this.issues = issuesResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInIssues() {
        this.eventSubscriber = this.eventManager.subscribe(
            'issuesListModification',
            (response) => this.load(this.issues.id)
        );
    }
}
