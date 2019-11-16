import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ProjectUsers } from './project-users.model';
import { ProjectUsersService } from './project-users.service';

@Component({
    selector: 'jhi-project-users-detail',
    templateUrl: './project-users-detail.component.html'
})
export class ProjectUsersDetailComponent implements OnInit, OnDestroy {

    projectUsers: ProjectUsers;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private projectUsersService: ProjectUsersService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProjectUsers();
    }

    load(id) {
        this.projectUsersService.find(id)
            .subscribe((projectUsersResponse: HttpResponse<ProjectUsers>) => {
                this.projectUsers = projectUsersResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProjectUsers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'projectUsersListModification',
            (response) => this.load(this.projectUsers.id)
        );
    }
}
