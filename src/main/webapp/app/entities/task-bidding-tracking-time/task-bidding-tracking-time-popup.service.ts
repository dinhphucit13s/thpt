import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { TaskBiddingTrackingTime } from './task-bidding-tracking-time.model';
import { TaskBiddingTrackingTimeService } from './task-bidding-tracking-time.service';

@Injectable()
export class TaskBiddingTrackingTimePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private taskBiddingTrackingTimeService: TaskBiddingTrackingTimeService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.taskBiddingTrackingTimeService.find(id)
                    .subscribe((taskBiddingTrackingTimeResponse: HttpResponse<TaskBiddingTrackingTime>) => {
                        const taskBiddingTrackingTime: TaskBiddingTrackingTime = taskBiddingTrackingTimeResponse.body;
                        taskBiddingTrackingTime.startTime = this.datePipe
                            .transform(taskBiddingTrackingTime.startTime, 'yyyy-MM-ddTHH:mm:ss');
                        taskBiddingTrackingTime.endTime = this.datePipe
                            .transform(taskBiddingTrackingTime.endTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.taskBiddingTrackingTimeModalRef(component, taskBiddingTrackingTime);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.taskBiddingTrackingTimeModalRef(component, new TaskBiddingTrackingTime());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    taskBiddingTrackingTimeModalRef(component: Component, taskBiddingTrackingTime: TaskBiddingTrackingTime): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.taskBiddingTrackingTime = taskBiddingTrackingTime;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
