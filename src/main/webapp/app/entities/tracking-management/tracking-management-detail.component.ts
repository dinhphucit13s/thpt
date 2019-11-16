import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import {AppConstants} from '../../shared/services/app-constants';
import {Packages} from '../packages/packages.model';
import { PackagesService } from './../packages/packages.service';
import {ButtonViewComponent} from '../../shared/dynamic-data-tables/buttons-view/button-view.component';
import {HomeService} from '../../home/home.service';

@Component({
    selector: 'jhi-tracking-management-detail',
    templateUrl: './tracking-management-detail.component.html'
})
export class TrackingManagementDetailComponent implements OnInit, OnDestroy {

    columnDefsArg: any;
    rowData: any;
    gridApi: any;
    gridColumnApi: any;
    frameworkComponents: any;
    projectId: number;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private route: ActivatedRoute,
        private packagesService: PackagesService,
        private homService: HomeService,
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.projectId = params['id'];
            this.load(this.projectId);
        });
        this.registerChangeInProjects();
        this.frameworkComponents = {
            actionsButton: ButtonViewComponent,
        };
    }

    load(id?: any) {
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjects() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projectsListModification',
            (response) => this.load()
        );
    }
}
