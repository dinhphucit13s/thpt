import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ProjectBugListDefault } from './project-bug-list-default.model';
import { ProjectBugListDefaultService } from './project-bug-list-default.service';

@Injectable()
export class ProjectBugListDefaultPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private projectBugListDefaultService: ProjectBugListDefaultService

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
                this.projectBugListDefaultService.find(id)
                    .subscribe((projectBugListDefaultResponse: HttpResponse<ProjectBugListDefault>) => {
                        const projectBugListDefault: ProjectBugListDefault = projectBugListDefaultResponse.body;
                        this.ngbModalRef = this.projectBugListDefaultModalRef(component, projectBugListDefault);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.projectBugListDefaultModalRef(component, new ProjectBugListDefault());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    projectBugListDefaultModalRef(component: Component, projectBugListDefault: ProjectBugListDefault): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.projectBugListDefault = projectBugListDefault;
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
