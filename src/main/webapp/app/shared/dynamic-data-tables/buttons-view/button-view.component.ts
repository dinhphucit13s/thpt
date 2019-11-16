import {Component} from '@angular/core';
import {ICellRendererAngularComp} from 'ag-grid-angular';
import { GridOptions } from 'ag-grid';
import {Router} from '@angular/router';
import {DataService} from '../../services/data.service';
import {Principal} from '../..';

@Component({
    selector: 'jhi-button-view',
    templateUrl: './button-view.component.html',
    styles: [
        `.btn {
            line-height: 0.5
        }`
    ]
})
export class ButtonViewComponent implements ICellRendererAngularComp {
    public params: any;
    public routers: any;
    public permission: number;
    namePro: any;
    taskStatus: any;
    isPM = false;
    gridOptions: GridOptions;
    currentAccount: any;

    constructor(private router: Router, private data: DataService, private principal: Principal) {

    }
    onGridReady(params) {
        params.columnApi.autoSizeColumn(params.columnApi.colId);
    }
    agInit(params: any): void {
        this.params = params;
        this.routers = params.routers;
        this.namePro = params.data.name;
        this.taskStatus = params.data.status;
        this.data.currentPermission.subscribe((permission) => {
            this.permission = permission;
            if (this.params.context.componentParent.permission && this.params.context.componentParent.permission > 0) {
                this.permission = this.params.context.componentParent.permission;
            }
            this.principal.identity().then((account) => {
                this.currentAccount = account;
                if (params.data.watcherUsers) {
                    if (params.data.watcherUsers.indexOf(this.currentAccount.login) > -1) {
                        this.permission = 1;
                    }
                }

                if (params.data.dedicatedUsers) {
                    if (params.data.dedicatedUsers.indexOf(this.currentAccount.login) > -1) {
                        this.permission = 3;

                    }
                }

                if (params.data.dtmsMonitoringProject) {
                    if (params.data.dtmsMonitoringProject.members === this.currentAccount.login
                        && params.data.dtmsMonitoringProject.role === 'ROLE_DEDICATED') {
                        this.permission = 3;
                    } else if (params.data.dtmsMonitoringProject.members === this.currentAccount.login
                        && params.data.dtmsMonitoringProject.role === 'ROLE_WATCHER') {
                        this.permission = 1;
                    }
                } else {
                    if (params.data.watcherUsersPO && params.data.watcherUsersPO !== null) {
                        if (params.data.watcherUsersPO.indexOf(this.currentAccount.login) > -1) {
                            this.permission = 1;
                        }
                    }

                    if (params.data.dedicatedUsersPO && params.data.dedicatedUsersPO !== null) {
                        if (params.data.dedicatedUsersPO.indexOf(this.currentAccount.login) > -1) {
                            this.permission = 3;
                        }
                    }
                }
            });
        });

        if ((this.params.context.componentParent.project) &&
            (this.params.data.id === this.params.context.componentParent.project.projectLeadId)) {
            this.isPM = true;
        }
        if (this.routers.viewMember) {
            if (this.router.routerState.snapshot.url.indexOf('/projects/') >= 0) {
                this.routers.viewMember = '../../project-member';
            }
        }
    }

    refresh(): boolean {
        return false;
    }

    onClickWorkflow(templateId) {
        // localStorage.setItem('currentTemplateId', templateId);
        // this.router.navigate(['/', { outlets: { popup: 'project-workflows/' + templateId + '/edit'} }]);
        this.router.navigate(['/project-workflows/' + templateId + '/edit']);
    }
}
