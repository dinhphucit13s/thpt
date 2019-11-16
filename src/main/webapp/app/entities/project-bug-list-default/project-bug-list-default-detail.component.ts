import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ProjectBugListDefault } from './project-bug-list-default.model';
import { ProjectBugListDefaultService } from './project-bug-list-default.service';

@Component({
    selector: 'jhi-project-bug-list-default-detail',
    templateUrl: './project-bug-list-default-detail.component.html'
})
export class ProjectBugListDefaultDetailComponent implements OnInit, OnDestroy {

    projectBugListDefault: ProjectBugListDefault;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private projectBugListDefaultService: ProjectBugListDefaultService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProjectBugListDefaults();
    }

    load(id) {
        this.projectBugListDefaultService.find(id)
            .subscribe((projectBugListDefaultResponse: HttpResponse<ProjectBugListDefault>) => {
                this.projectBugListDefault = projectBugListDefaultResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjectBugListDefaults() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projectBugListDefaultListModification',
            (response) => this.load(this.projectBugListDefault.id)
        );
    }
}
