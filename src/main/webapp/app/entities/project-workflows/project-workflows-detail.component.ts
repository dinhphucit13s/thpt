import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ProjectWorkflows } from './project-workflows.model';
import { ProjectWorkflowsService } from './project-workflows.service';

@Component({
    selector: 'jhi-project-workflows-detail',
    templateUrl: './project-workflows-detail.component.html'
})
export class ProjectWorkflowsDetailComponent implements OnInit, OnDestroy {

    projectWorkflows: ProjectWorkflows;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private projectWorkflowsService: ProjectWorkflowsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProjectWorkflows();
    }

    load(id) {
        this.projectWorkflowsService.find(id)
            .subscribe((projectWorkflowsResponse: HttpResponse<ProjectWorkflows>) => {
                this.projectWorkflows = projectWorkflowsResponse.body;
            });
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

    registerChangeInProjectWorkflows() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projectWorkflowsListModification',
            (response) => this.load(this.projectWorkflows.id)
        );
    }
}
