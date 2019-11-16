import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { BugListDefault } from './bug-list-default.model';
import { BugListDefaultService } from './bug-list-default.service';

@Component({
    selector: 'jhi-bug-list-default-detail',
    templateUrl: './bug-list-default-detail.component.html'
})
export class BugListDefaultDetailComponent implements OnInit, OnDestroy {

    bugListDefault: BugListDefault;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private bugListDefaultService: BugListDefaultService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBugListDefaults();
    }

    load(id) {
        this.bugListDefaultService.find(id)
            .subscribe((bugListDefaultResponse: HttpResponse<BugListDefault>) => {
                this.bugListDefault = bugListDefaultResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBugListDefaults() {
        this.eventSubscriber = this.eventManager.subscribe(
            'bugListDefaultListModification',
            (response) => this.load(this.bugListDefault.id)
        );
    }
}
