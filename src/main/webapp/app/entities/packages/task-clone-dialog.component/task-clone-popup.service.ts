import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { TaskBiddingService} from '../../task-bidding';
import { TaskBidding} from '../../task-bidding';

@Injectable()
export class TaskClonePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private taskBiddingService: TaskBiddingService

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
                this.taskBiddingService.find(id)
                    .subscribe((taskBiddingResponse: HttpResponse<TaskBidding>) => {
                        const taskBidding: TaskBidding = taskBiddingResponse.body;
                        this.ngbModalRef = this.taskBiddingModalRef(component, taskBidding);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.taskBiddingModalRef(component, new TaskBidding());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    taskBiddingModalRef(component: Component, taskBidding: TaskBidding): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.taskBidding = taskBidding;
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
