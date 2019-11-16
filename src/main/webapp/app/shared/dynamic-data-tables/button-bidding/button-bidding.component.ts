import {Component} from '@angular/core';
import {ICellRendererAngularComp} from 'ag-grid-angular';
import { GridOptions } from 'ag-grid';
import {Router} from '@angular/router';
import {DataService} from '../../services/data.service';

@Component({
    selector: 'jhi-button-bidding',
    templateUrl: './button-bidding.component.html',
    styles: [
        `.btn {
            line-height: 0.5
        }`
    ]
})
export class ButtonBiddingComponent implements ICellRendererAngularComp {
    public params: any;
    public routers: any;
    public permission: number;
    namePro: any;
    taskStatus: any;
    isPM = false;
    gridOptions: GridOptions;
    constructor(private router: Router, private data: DataService) {

    }
    agInit(params: any): void {
        this.params = params;
        this.routers = params.routers;
        this.namePro = params.data.name;
        this.taskStatus = params.data.status;
        this.data.currentPermission.subscribe((permission) => this.permission = permission);

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
        sessionStorage.setItem('currentTemplateId', templateId);
        this.router.navigate(['/', { outlets: { popup: 'project-workflows/' + templateId + '/edit'} }]);
    }
}
