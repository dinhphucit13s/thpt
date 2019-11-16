import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TaskTrackingTime } from './task-tracking-time.model';
import { TaskTrackingTimeService } from './task-tracking-time.service';

@Component({
    selector: 'jhi-task-tracking-time-detail',
    templateUrl: './task-tracking-time-detail.component.html'
})
export class TaskTrackingTimeDetailComponent implements OnInit, OnDestroy {

    taskTrackingTime: TaskTrackingTime;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private taskTrackingTimeService: TaskTrackingTimeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTaskTrackingTimes();
    }

    load(id) {
        this.taskTrackingTimeService.find(id)
            .subscribe((taskTrackingTimeResponse: HttpResponse<TaskTrackingTime>) => {
                this.taskTrackingTime = taskTrackingTimeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTaskTrackingTimes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'taskTrackingTimeListModification',
            (response) => this.load(this.taskTrackingTime.id)
        );
    }
}
