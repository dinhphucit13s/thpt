import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager, JhiParseLinks} from 'ng-jhipster';
import {ActivatedRoute, Router} from '@angular/router';
import {Account, ITEMS_PER_PAGE, LoginModalService, Principal} from '../shared';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import {Projects} from '../entities/projects/projects.model';
import {ProjectsService} from '../entities/projects/projects.service';
import {HomeService} from './home.service';
import {GridOptions} from 'ag-grid';
import {CustomReportsService} from '../entities/custom-reports/custom-reports.service';
import {CustomReports, ValueReport} from '../entities/custom-reports/custom-reports.model';
import {ProjectUsers} from '../entities/project-users/project-users.model';
require('../../content/js/chart/amchart/amcharts/amcharts.js');
require('../../content/js/chart/amchart/amcharts/serial.js');
require('../../content/js/chart/amchart/amcharts/pie.js');
require('../../content/js/chart/amchart/amcharts/serial.js');
require('../../content/js/chart/amchart/amcharts/radar.js');
declare var AmCharts: any;
declare var $: any;
@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit {
    filter: any = 'all';

    account: Account;
    modalRef: NgbModalRef;
    projects: Projects[];
    project: Projects;
    projectId: any = 0;
    projectName: any;
    totalResources: any = 0;
    totalPuchaseOrders: any = 0;
    totalPackages: any = 0;
    totalTasks: any = 0;

    columnDefsArg: any;
    rowData: any = [];
    gridApi: any;
    gridColumnApi: any;
    gridOptions: GridOptions;
    frameworkComponents: any;
    taskCcolumnDefsArg: any;
    taskRowData: any = [];
    unassignTaskCcolumnDefsArg: any;
    unassignTaskRowData: any = [];

    /*page*/
    itemsPerPage: any;
    routeData: any;
    page: any;
    previousPage: any;
    reverse: any;
    predicate: any;
    links: any;
    totalItems: any;
    queryCount: any;
    /*pageReports*/
    customReports: any;
    package_late: any;
    task_late: any;
    task_un_assign: any;
    valueReports: any;
    valueSelect = new Array();
    dropdownSettings = {};
    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private router: Router,
        private jhiAlertService: JhiAlertService,
        private projectsService: ProjectsService,
        private customReportsService: CustomReportsService,
        private homService: HomeService,
        private activatedRoute: ActivatedRoute,
        private parseLinks: JhiParseLinks,
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.package_late = false;
        this.task_late = false;
        this.task_un_assign = false;
        this.valueReports = ['Packages_Late', 'Tasks_Late', 'Tasks Un-Assign'];
        this.customReports = new CustomReports();
        this.customReports.value = new Array();
    }

    ngOnInit() {
        this.dropdownSettings = {
            singleSelection: false,
            itemsShowLimit: 1,
            allowSearchFilter: false,
            enableCheckAll: false,
            limitSelection: 100
        };
        this.principal.identity().then((account) => {
            this.account = account;
            if (!this.principal.isAuthenticated()) {
                this.router.navigate(['/login']);
            }
            this.loadListProjects(this.account.login);
            this.customReportsService.query('Dashboard', this.account.login).subscribe((res) => {
                if (res.body) {
                    this.customReports = res.body;
                    this.customReports.value = JSON.parse(this.customReports.value);
                }
                this.mapValueToList();
            });
        });
        this.registerAuthenticationsuccessFull();
    }
    onItemSelect(value: any) {
        if (value === 'Packages_Late') {
            this.package_late = true;
        }
        if (value === 'Tasks_Late') {
            this.task_late = true;
        }
        if (value === 'Tasks Un-Assign') {
            this.task_un_assign = true;
        }
    }

    onDeSelect(value: any) {
        if (value === 'Packages_Late') {
            this.package_late = false;
        }
        if (value === 'Tasks_Late') {
            this.task_late = false;
        }
        if (value === 'Tasks Un-Assign') {
            this.task_un_assign = false;
        }
    }
    mapValueToList() {
        this.customReports.value.forEach((value) => {
            if (value === 'Packages_Late') {
                this.package_late = true;
            }else if (value === 'Tasks_Late') {
                this.task_late = true;
            }else if (value === 'Tasks Un-Assign') {
                this.task_un_assign = true;
            }
        });
    }
    saveReport() {
        if (this.customReports.id !== undefined) {
            this.customReportsService.update(this.customReports).subscribe((res) => {
                return res;
            });
        } else {
            this.customReports.userLogin = this.account.login;
            this.customReports.pageName = 'Dashboard';
            this.customReportsService.create(this.customReports).subscribe((res) => {
                this.customReports = res.body;
            });
        }
    }
    loadPage(page: number) {
        this.page = page;
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/'], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        // this.loadAll();
        this.loadDataByProject();
    }
    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
    onClickPurchaseOrder(proId: number) {
        this.router.navigate(['/purchase-orders'], {queryParams: {dashboardProId: proId}});
    }

    onClickResource() {
        this.router.navigate(['/project-users-list/' + this.projectId + '/' + this.projectName], {queryParams: {projectId: this.projectId, projectName: this.projectName }});
    }
    // loadLatePackageColumn(id) {
    //     this.homService.loadLatePackageColumn(id)
    //         .subscribe((res: HttpResponse<any>) => {
    //             this.columnDefsArg = res.body;
    //         });
    // }
    //
    // loadLatePackageData(id) {
    //     this.homService.loadLatePackageData(id)
    //         .subscribe((res: HttpResponse<any>) => {
    //             this.rowData = res.body;
    //         });
    // }
    //
    // loadLateTaskColumn(id) {
    //     this.homService.loadLateTaskColumn(id)
    //         .subscribe((res: HttpResponse<any>) => {
    //             this.taskCcolumnDefsArg = res.body;
    //         });
    // }
    //
    // loadLateTaskData(id) {
    //     this.homService.loadLateTaskData({
    //         page: this.page - 1,
    //         size: 1000,
    //         sort: this.sort()}, id)
    //         .subscribe((res: HttpResponse<any>) => {
    //             this.taskRowData = res.body;
    //             this.onSuccess(res.body, res.headers);
    //         });
    // }
    //
    // loadUnAssignTaskColumn(id) {
    //     this.homService.loadUnAssignTaskColumn(id)
    //         .subscribe((res: HttpResponse<any>) => {
    //             this.unassignTaskCcolumnDefsArg = res.body;
    //         });
    // }
    //
    // loadUnAssignTaskData(id) {
    //     this.homService.loadUnAssignTaskData(id)
    //         .subscribe((res: HttpResponse<any>) => {
    //             this.unassignTaskRowData = res.body;
    //         });
    // }

    getSizePurchaseOrdersRelatingToProject() {
        this.homService.getSizePurchaseOrdersRelatingToProject(this.projectId)
            .subscribe((res: HttpResponse<any>) => {
                this.totalPuchaseOrders = res.body;
            });
    }

    getSizeTasksRelatingToProject() {
        this.homService.getSizeTasksRelatingToProject(this.projectId)
            .subscribe((res: HttpResponse<any>) => {
                this.totalTasks = res.body;
            });
    }

    getSizePackageRelatingToProject() {
        this.homService.getSizePackageRelatingToProject(this.projectId)
            .subscribe((res: HttpResponse<any>) => {
                this.totalPackages = res.body;
            });
    }

    getSizeUserRelatingToProject() {
        this.homService.getSizeUserRelatingToProject(this.projectId)
            .subscribe((res: HttpResponse<any>) => {
                this.totalResources = res.body;
            });
    }

    registerAuthenticationsuccessFull() {
        this.eventManager.subscribe('authenticationsuccessFull', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    loadListProjects(userLogin: any) {
        /*this.projectsService.getListProjectByUserLogin(userLogin).subscribe(
            (res: HttpResponse<Projects[]>) => {
                this.projects = res.body;
                this.projectId = this.projects[0].id;
                this.projectName = this.projects[0].name;
                this.loadDataByProject();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );*/
        this.projectsService.queryProjectWithMonitoring({
            page: 0,
            size: 1000000,
            sort: this.sort()}).subscribe(
            (res: HttpResponse<Projects[]>) => {
                this.projects = res.body;
                this.projectId = this.projects[0].id;
                this.projectName = this.projects[0].name;
                this.loadDataByProject();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadDataByProject() {
        this.getSizePackageRelatingToProject();
        this.getSizePurchaseOrdersRelatingToProject();
        this.getSizeTasksRelatingToProject();
        this.getSizeUserRelatingToProject();
        if (this.projectId !== 0) {
            // this.loadLateTaskData(this.projectId);
            // this.loadLateTaskColumn(this.projectId);
            // this.loadLatePackageColumn(this.projectId);
            // this.loadLatePackageData(this.projectId);
            // this.loadUnAssignTaskData(this.projectId);
            // this.loadUnAssignTaskColumn(this.projectId);
            sessionStorage.setItem('currentProjectId', this.projectId);
            // set new projectName by ID when click new Project
            this.projectsService.getProject(this.projectId).subscribe((res: HttpResponse<Projects>) => {
                this.project = res.body;
                this.projectName = this.project.name;
            });
        }
        console.log(this.projectName);
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }
    login() {
        this.modalRef = this.loginModalService.open();
    }

    trackProjectById(index: number, item: Projects) {
        this.projectName = item.name;
        return item.id;
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    onFilterTasksChange() {
        console.log(this.filter);
        console.log(this.projectId);
        this.homService.getTasksByFilter(this.filter, this.projectId).subscribe(
            (res) => {
                console.log(res.body);
            }
        );
    }

    getProjectName(proName: String) {
        this.projectName = proName;
    }

    loadProjectName() {

    }
}
