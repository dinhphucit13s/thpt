import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Bugs } from './bugs.model';
import { BugsService } from './bugs.service';

@Component({
    selector: 'jhi-bugs-detail',
    templateUrl: './bugs-detail.component.html'
})
export class BugsDetailComponent implements OnInit, OnDestroy {

    bugs: Bugs;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private bugsService: BugsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBugs();
    }

    load(id) {
        this.bugsService.find(id)
            .subscribe((bugsResponse: HttpResponse<Bugs>) => {
                this.bugs = bugsResponse.body;
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

    registerChangeInBugs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'bugsListModification',
            (response) => this.load(this.bugs.id)
        );
    }
}
