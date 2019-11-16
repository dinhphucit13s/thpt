import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ProjectTemplates } from './project-templates.model';
import { ProjectTemplatesService } from './project-templates.service';

@Component({
    selector: 'jhi-project-templates-detail',
    templateUrl: './project-templates-detail.component.html'
})
export class ProjectTemplatesDetailComponent implements OnInit, OnDestroy {

    projectTemplates: ProjectTemplates;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private projectTemplatesService: ProjectTemplatesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProjectTemplates();
    }

    load(id) {
        this.projectTemplatesService.find(id)
            .subscribe((projectTemplatesResponse: HttpResponse<ProjectTemplates>) => {
                this.projectTemplates = projectTemplatesResponse.body;
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

    registerChangeInProjectTemplates() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projectTemplatesListModification',
            (response) => this.load(this.projectTemplates.id)
        );
    }
}
