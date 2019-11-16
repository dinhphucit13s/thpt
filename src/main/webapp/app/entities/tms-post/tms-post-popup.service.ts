import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { TmsPost } from './tms-post.model';
import { TmsPostService } from './tms-post.service';

@Injectable()
export class TmsPostPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private tmsPostService: TmsPostService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any, threadId?: number): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.tmsPostService.find(id)
                    .subscribe((tmsPostResponse: HttpResponse<TmsPost>) => {
                        const tmsPost: TmsPost = tmsPostResponse.body;
                        this.ngbModalRef = this.tmsPostModalRef(component, tmsPost);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    const tmsPost: TmsPost = new TmsPost();
                    tmsPost.threadId = threadId;
                    this.ngbModalRef = this.tmsPostModalRef(component, tmsPost);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    tmsPostModalRef(component: Component, tmsPost: TmsPost): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.tmsPost = tmsPost;
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
