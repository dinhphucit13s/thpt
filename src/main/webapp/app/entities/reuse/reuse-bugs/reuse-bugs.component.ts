import { Component, OnInit, OnDestroy, Input, Output } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Bugs } from '../../bugs/bugs.model';
import { BugsService } from '../../bugs/bugs.service';
import { ITEMS_PER_PAGE, Principal } from '../../../shared';
import {PermissionService} from '../../../account/login/permission.service';
import {GridOptions} from 'ag-grid';
import {AppConstants} from '../../../shared/services/app-constants';
import {NumericEditorComponent} from '../../../shared/dynamic-data-tables/numeric-editor/numeric-editor.component';
import {ButtonViewComponent} from '../../../shared/dynamic-data-tables/buttons-view/button-view.component';

declare var $: any;
@Component({
    selector: 'reuse-bugs',
    templateUrl: './reuse-bugs.component.html'
})
export class ReuseBugsComponent implements OnInit, OnDestroy {

    private tasks: any = {};
    strDirectLink = '../bugs';
    strBackURL = 'bugs';

    currentAccount: any;
    bugs: Bugs[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    /*data table*/
    frameworkComponents: any;
    columnDefs: any;
    rowData: any;
    gridApi: any;
    gridColumnApi: any;
    gridOptions: GridOptions;

    @Input() taskId;
    @Input()
    set passRoute(data: string) {
        if (data !== '' && data !== undefined) {
            this.strDirectLink = '../../bugs';
            this.tasks = JSON.parse(this.replaceAll(data, "'", '"'));
            this.strBackURL = 'tasks/' + this.tasks.id;
        }
    }
    get passRoute() {
        return this.tasks;
    }
    replaceAll(strIn: string, strFind: string, strReplace: string) {
        return strIn.replace(new RegExp(strFind.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&'), 'g'), strReplace);
    }

    constructor(
        private bugsService: BugsService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            /*this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;*/
            this.page = 1;
            this.previousPage = 1;
            this.reverse = true;
            this.predicate = 'id';
        });
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
        this.frameworkComponents = {
            actionsButton: ButtonViewComponent
        };
    }

    loadAll() {
        if (this.currentSearch) {
            this.bugsService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                (res: HttpResponse<Bugs[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
            return;
        }
        this.bugsService.queryListBugByTaskId({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}, this.taskId).subscribe(
            (res: HttpResponse<Bugs[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        const item: any = {
            page: this.page,
            size: this.itemsPerPage,
            search: this.currentSearch,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }
        /*this.router.navigate(['/bugs'], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    search: this.currentSearch,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });*/
        if (this.tasks.id === undefined) {
            this.router.navigate(['/bugs'], {
                queryParams: item
            });
        } else {
            this.router.navigate(['/' + this.tasks.route + '/' + this.tasks.id], {
                queryParams: item
            });
        }
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/bugs', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/bugs', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.columnDefs = AppConstants.BugItems;
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInBugs();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Bugs) {
        return item.id;
    }
    registerChangeInBugs() {
        this.eventSubscriber = this.eventManager.subscribe('bugsListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.bugs = data;
        this.rowData = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
