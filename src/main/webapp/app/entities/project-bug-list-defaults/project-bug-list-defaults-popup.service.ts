import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Projects } from '../projects';
import { ProjectsService } from '../projects';
import {BugListDefault, BugListDefaultService} from '../bug-list-default';
import {ProjectUsers} from '../project-users/project-users.model';
import {ProjectBugListDefault} from '../project-bug-list-default';
import {ProjectBugListDefaults} from './project-bug-list-defaults.model';
import { ProjectBugListDefaultsService } from './project-bug-list-defaults.service';

@Injectable()
export class ProjectBugListDefaultsPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private projectsService: ProjectsService,
        private bugListDefaultsService: BugListDefaultService,
        private projectBugListDefaultsService: ProjectBugListDefaultsService

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
                this.projectBugListDefaultsService.find(id)
                    .subscribe((projectBugListDefaultResponse: HttpResponse<ProjectBugListDefaults>) => {
                        const projectBugListDefault: ProjectBugListDefaults = projectBugListDefaultResponse.body;
                        this.ngbModalRef = this.projectBugListDefaultModalRef(component, projectBugListDefault);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.projectBugListDefaultModalRef(component, new ProjectBugListDefaults());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    openBugListDefaults(component: Component, id: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.bugListDefaultsService.find(id)
                    .subscribe((responseProjects: HttpResponse<any>) => {
                        const bugListDefaults: any = responseProjects.body;
                        this.ngbModalRef = this.projectBugListDefaultsModalRef(component, id);
                        resolve(this.ngbModalRef);
                    });

            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.projectBugListDefaultsModalRef(component, new BugListDefault());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    openNew(component: Component, id?: number | any, name?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
            setTimeout(() => {
                const pd = new ProjectBugListDefaults();
                pd.projectId = id;
                pd.projectName = name;
                this.ngbModalRef = this.projectBugListDefaultsModalRefCP(component, pd);
                resolve(this.ngbModalRef);
            }, 0);
        });
    }

    projectBugListDefaultsModalRefCP(component: Component, projectBugListDefault: ProjectBugListDefaults): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.projectBug = projectBugListDefault;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }

    projectBugListDefaultModalRef(component: Component, projectBugListDefault: ProjectBugListDefaults): NgbModalRef {
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

    projectBugListDefaultsModalRef(component: Component, bugListDefault: any, projectId?: any): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.bugListDefault = bugListDefault;
        modalRef.componentInstance.projectId = projectId;
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
