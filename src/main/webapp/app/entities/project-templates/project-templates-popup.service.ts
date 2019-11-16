import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ProjectTemplates } from './project-templates.model';
import { ProjectTemplatesService } from './project-templates.service';

@Injectable()
export class ProjectTemplatesPopupService {
    private ngbModalRef: NgbModalRef;
    private stepNgbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private projectTemplatesService: ProjectTemplatesService

    ) {
        this.ngbModalRef = null;
        this.stepNgbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.projectTemplatesService.find(id)
                    .subscribe((projectTemplatesResponse: HttpResponse<ProjectTemplates>) => {
                        const projectTemplates: ProjectTemplates = projectTemplatesResponse.body;
                        this.ngbModalRef = this.projectTemplatesModalRef(component, projectTemplates);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.projectTemplatesModalRef(component, new ProjectTemplates());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    projectTemplatesModalRef(component: Component, projectTemplates: ProjectTemplates): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.projectTemplates = projectTemplates;
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
