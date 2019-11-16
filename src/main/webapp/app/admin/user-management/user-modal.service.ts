import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { User, UserService } from '../../shared';
import {UserProfile} from '../../shared/user-profile/user-profile.model';

@Injectable()
export class UserModalService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private userService: UserService
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
                this.userService.find(id).subscribe((response) => {
                    const user: User = response.body;
                    if (user.userProfile === undefined || user.userProfile === null) {
                        user.userProfile = new UserProfile();
                    }
                    if (user.dob) {
                        user.dob = {
                            year: user.dob.getFullYear(),
                            month: user.dob.getMonth() + 1,
                            day: user.dob.getDate()
                        };
                    }
                    this.ngbModalRef = this.userModalRef(component, user);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.userModalRef(component, new User());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    openChange(component: Component, userCurrent?: any | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (userCurrent) {
                let user: User = new User();
                user.login = userCurrent;
                this.ngbModalRef = this.userModalRef(component, user);
                resolve(this.ngbModalRef);
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.userModalRef(component, new User());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    userModalRef(component: Component, user: User): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.user = user;
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
