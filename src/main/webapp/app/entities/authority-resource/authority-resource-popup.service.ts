import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { AuthorityResource } from './authority-resource.model';
import { AuthorityResourceService } from './authority-resource.service';
import { Role } from './role.model'
import { RolePermission } from './role-permission.model'
import { DataService } from '../../shared/services/data.service';

@Injectable()
export class AuthorityResourcePopupService {
    private ngbModalRef: NgbModalRef;
    private resources: RolePermission[];

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private roleService: AuthorityResourceService,
        private data: DataService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: string): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }
            if (id) {
                this.resources = new Array<RolePermission>();
                this.roleService.queryResourceGroups().subscribe((groupResponse: HttpResponse<string[]>) => {
                    for (let entry of groupResponse.body) {
                        let newEntry = new RolePermission();
                        newEntry.resource = entry;
                        newEntry.permission = 0;
                        this.resources.push(newEntry);
                    }
                    this.roleService.find(id)
                    .subscribe((roleResponse: HttpResponse<Role>) => {
                        const role: Role = roleResponse.body;
                        for (let savedResource of role.resources ) {
                            for (let viewResource of this.resources) {
                                if (viewResource.resource == savedResource.name) {
                                    viewResource.permission = savedResource.permission;
                                }
                            }
                        }
                        this.data.changeRolePermissions(this.resources);
                        this.ngbModalRef = this.roleModalRef(component, role, this.resources);
                        resolve(this.ngbModalRef);
                    });
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                this.resources = new Array<RolePermission>()
                setTimeout(() => {
                    this.roleService.queryResourceGroups().subscribe((groupResponse: HttpResponse<string[]>) => {
                        for (let entry of groupResponse.body) {
                            let newEntry = new RolePermission();
                            newEntry.resource = entry;
                            newEntry.permission = 0;
                            this.resources.push(newEntry);
                        }
                        this.data.changeRolePermissions(this.resources);
                        this.ngbModalRef = this.roleModalRef(component, new Role(), this.resources);
                    resolve(this.ngbModalRef);
                    });
                }, 0);
            }
        });
    }

    roleModalRef(component: Component, role: Role, resources: RolePermission[]): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.role = role;
        modalRef.componentInstance.resources = resources;
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
