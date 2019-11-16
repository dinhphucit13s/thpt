import { Injectable, Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Tasks } from './tasks.model';
import { TasksService } from './tasks.service';
import {Packages} from '../packages/packages.model';

@Injectable()
export class TasksPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private tasksService: TasksService,
        private activatedRoute: ActivatedRoute,

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
                this.tasksService.find(id)
                    .subscribe((tasksResponse: HttpResponse<Tasks>) => {
                        const tasks: Tasks = tasksResponse.body;
                        tasks.estimateStartTime = this.datePipe
                            .transform(tasks.estimateStartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.estimateEndTime = this.datePipe
                            .transform(tasks.estimateEndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.opStartTime = this.datePipe
                            .transform(tasks.opStartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.opEndTime = this.datePipe
                            .transform(tasks.opEndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.review1StartTime = this.datePipe
                            .transform(tasks.review1StartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.review1EndTime = this.datePipe
                            .transform(tasks.review1EndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.fixStartTime = this.datePipe
                            .transform(tasks.fixStartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.fixEndTime = this.datePipe
                            .transform(tasks.fixEndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.review2StartTime = this.datePipe
                            .transform(tasks.review2StartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.review2EndTime = this.datePipe
                            .transform(tasks.review2EndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.fiStartTime = this.datePipe
                            .transform(tasks.fiStartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.fiEndTime = this.datePipe
                            .transform(tasks.fiEndTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.tasksModalRef(component, tasks);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.tasksModalRef(component, new Tasks());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    openTask(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }
            if (id) {
                setTimeout(() => {
                    const task: Tasks = new Tasks();
                    task.packagesId = id;
                    this.ngbModalRef = this.tasksModalRef(component, task);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    openClone(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.tasksService.find(id)
                    .subscribe((tasksResponse: HttpResponse<Tasks>) => {
                        const tasks: Tasks = tasksResponse.body;
                        tasks.estimateStartTime = this.datePipe
                            .transform(tasks.estimateStartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.estimateEndTime = this.datePipe
                            .transform(tasks.estimateEndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.opStartTime = this.datePipe
                            .transform(tasks.opStartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.opEndTime = this.datePipe
                            .transform(tasks.opEndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.review1StartTime = this.datePipe
                            .transform(tasks.review1StartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.review1EndTime = this.datePipe
                            .transform(tasks.review1EndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.fixStartTime = this.datePipe
                            .transform(tasks.fixStartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.fixEndTime = this.datePipe
                            .transform(tasks.fixEndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.review2StartTime = this.datePipe
                            .transform(tasks.review2StartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.review2EndTime = this.datePipe
                            .transform(tasks.review2EndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.fiStartTime = this.datePipe
                            .transform(tasks.fiStartTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.fiEndTime = this.datePipe
                            .transform(tasks.fiEndTime, 'yyyy-MM-ddTHH:mm:ss');
                        tasks.parent = id;
                        this.ngbModalRef = this.tasksModalRefClone(component, tasks);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.tasksModalRef(component, new Tasks());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    openImport(component: Component, package_id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }
            if (package_id) {
                setTimeout(() => {
                    this.ngbModalRef = this.tasksModalRefImport(component, package_id);
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    tasksModalRef(component: Component, tasks: Tasks): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.tasks = tasks;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
    tasksModalRefClone(component: Component, tasks: Tasks): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        tasks.id = null;
        modalRef.componentInstance.tasks = tasks;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }

    tasksModalRefImport(component: Component, package_id?: number | any): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.package_id = package_id;
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
