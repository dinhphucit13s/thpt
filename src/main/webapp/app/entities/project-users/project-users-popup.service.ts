import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ProjectUsers } from './project-users.model';
import { ProjectUsersService } from './project-users.service';

@Injectable()
export class ProjectUsersPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private projectUsersService: ProjectUsersService

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
                this.projectUsersService.find(id)
                    .subscribe((projectUsersResponse: HttpResponse<ProjectUsers>) => {
                        const projectUsers: ProjectUsers = projectUsersResponse.body;
                        this.ngbModalRef = this.projectUsersModalRef(component, projectUsers);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.projectUsersModalRef(component, new ProjectUsers());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    openNew(component: Component, id?: number | any,name?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
            setTimeout(() => {
                const pu = new ProjectUsers();
                pu.projectId = id;
                pu.projectName = name;
                this.ngbModalRef = this.projectUsersModalRef(component, pu);
                resolve(this.ngbModalRef);
            }, 0);
        });
    }

    projectUsersModalRef(component: Component, projectUsers: ProjectUsers): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.projectUsers = projectUsers;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }

    openImport(component: Component, id?: number | any, name?: number | any): Promise<NgbModalRef>  {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
            setTimeout(() => {
                const pu = new ProjectUsers();
                pu.projectId = id;
                pu.projectName = name;
                this.ngbModalRef = this.projectUsersImportModalRef(component, pu);
                resolve(this.ngbModalRef);
            }, 0);
        });
    }

    projectUsersImportModalRef(component: Component, projectUsers: ProjectUsers): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.projectUsers = projectUsers;
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
