import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { TaskTrackingTime } from './task-tracking-time.model';
import { TaskTrackingTimeService } from './task-tracking-time.service';

@Injectable()
export class TaskTrackingTimePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private taskTrackingTimeService: TaskTrackingTimeService

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
                this.taskTrackingTimeService.find(id)
                    .subscribe((taskTrackingTimeResponse: HttpResponse<TaskTrackingTime>) => {
                        const taskTrackingTime: TaskTrackingTime = taskTrackingTimeResponse.body;
                        taskTrackingTime.startTime = this.datePipe
                            .transform(taskTrackingTime.startTime, 'yyyy-MM-ddTHH:mm:ss');
                        taskTrackingTime.endTime = this.datePipe
                            .transform(taskTrackingTime.endTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.taskTrackingTimeModalRef(component, taskTrackingTime);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.taskTrackingTimeModalRef(component, new TaskTrackingTime());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    taskTrackingTimeModalRef(component: Component, taskTrackingTime: TaskTrackingTime): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.taskTrackingTime = taskTrackingTime;
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
