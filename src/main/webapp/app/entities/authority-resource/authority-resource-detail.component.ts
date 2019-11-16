import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { AuthorityResource } from './authority-resource.model';
import { AuthorityResourceService } from './authority-resource.service';

@Component({
    selector: 'jhi-authority-resource-detail',
    templateUrl: './authority-resource-detail.component.html'
})
export class AuthorityResourceDetailComponent implements OnInit, OnDestroy {

    authorityResource: AuthorityResource;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private authorityResourceService: AuthorityResourceService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['name']);
        });
        this.registerChangeInAuthorityResources();
    }

    load(id) {
        this.authorityResourceService.find(name)
            .subscribe((authorityResourceResponse: HttpResponse<AuthorityResource>) => {
                this.authorityResource = authorityResourceResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAuthorityResources() {
        this.eventSubscriber = this.eventManager.subscribe(
            'authorityResourceListModification',
            (response) => this.load(this.authorityResource.name)
        );
    }
}
