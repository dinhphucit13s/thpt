import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TaskBidding } from './task-bidding.model';
import { TaskBiddingService } from './task-bidding.service';

@Component({
    selector: 'jhi-task-bidding-detail',
    templateUrl: './task-bidding-detail.component.html'
})
export class TaskBiddingDetailComponent implements OnInit, OnDestroy {

    taskBidding: TaskBidding;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private taskBiddingService: TaskBiddingService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTaskBiddings();
    }

    load(id) {
        this.taskBiddingService.find(id)
            .subscribe((taskBiddingResponse: HttpResponse<TaskBidding>) => {
                this.taskBidding = taskBiddingResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTaskBiddings() {
        this.eventSubscriber = this.eventManager.subscribe(
            'taskBiddingListModification',
            (response) => this.load(this.taskBidding.id)
        );
    }
}
