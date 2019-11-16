import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Projects } from './projects.model';
import { ProjectsService } from './projects.service';

@Injectable()
export class ProjectsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private projectsService: ProjectsService

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
                this.projectsService.find(id)
                    .subscribe((projectsResponse: HttpResponse<Projects>) => {
                        const projects: Projects = projectsResponse.body;
                        projects.startTime = this.datePipe
                            .transform(projects.startTime, 'yyyy-MM-ddTHH:mm:ss');
                        projects.endTime = this.datePipe
                            .transform(projects.endTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.projectsModalRef(component, projects);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.projectsModalRef(component, new Projects());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    projectsModalRef(component: Component, projects: Projects): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.projects = projects;
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
