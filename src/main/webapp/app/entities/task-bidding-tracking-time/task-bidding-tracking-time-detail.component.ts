import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TaskBiddingTrackingTime } from './task-bidding-tracking-time.model';
import { TaskBiddingTrackingTimeService } from './task-bidding-tracking-time.service';

@Component({
    selector: 'jhi-task-bidding-tracking-time-detail',
    templateUrl: './task-bidding-tracking-time-detail.component.html'
})
export class TaskBiddingTrackingTimeDetailComponent implements OnInit, OnDestroy {

    taskBiddingTrackingTime: TaskBiddingTrackingTime;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private taskBiddingTrackingTimeService: TaskBiddingTrackingTimeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTaskBiddingTrackingTimes();
    }

    load(id) {
        this.taskBiddingTrackingTimeService.find(id)
            .subscribe((taskBiddingTrackingTimeResponse: HttpResponse<TaskBiddingTrackingTime>) => {
                this.taskBiddingTrackingTime = taskBiddingTrackingTimeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTaskBiddingTrackingTimes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'taskBiddingTrackingTimeListModification',
            (response) => this.load(this.taskBiddingTrackingTime.id)
        );
    }
}
