import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Item } from '../services/item.model';
import {ProjectWorkflows, TaskWorkflowVM} from '../../entities/project-workflows';
import {DomSanitizer} from '@angular/platform-browser';
import { Lightbox } from 'ngx-lightbox';
import {forEach} from '@angular/router/src/utils/collection';

@Component({
  selector: 'jhi-workflow-process',
  templateUrl: './workflow-process.component.html',
    styleUrls: ['./workflow-process.component.css'],
  styles: []
})
export class WorkflowProcessComponent implements OnInit {
  @Input() workflowObject: ProjectWorkflows;
  @Input() taskWorkflowVM: TaskWorkflowVM[];
  @Output() detectChange: EventEmitter<ProjectWorkflows>;
  private _albums: any = [];

  constructor( private sanitizer: DomSanitizer, private _lightbox: Lightbox
  ) {
    this.detectChange = new EventEmitter<ProjectWorkflows>();
  }

  onViewChange() {
      this.detectChange.emit(this.workflowObject);
  }

  ngOnInit() {
      console.log(this.taskWorkflowVM);
      this.taskWorkflowVM.forEach((item) => {
          const album = {
              src: 'data:image/png' + ';base64,' + item.image
          };

          this._albums.push(album);
      });
  }

    sanitize(url: string) {
        return this.sanitizer.bypassSecurityTrustUrl(url);
    }

    setSelectedProcess(process: any) {
      this.workflowObject.activity = process.processKey;
      this.workflowObject.entityDTO.forEach((item) => {
        if (item.properties.isActivityField) {
            item.properties.isActiveOnPM = false;
            item.properties.isRequiredOnPM = false;
            item.properties.isActiveOnOP = false;
        }
      });
      process.workflowItems.forEach((el) => {
        this.workflowObject.entityDTO.forEach((item) => {
            if (item.properties.isActivityField) {
                if (item.field === el || item.field === el + 'Status' || item.field === el + 'StartTime' || item.field === el + 'EndTime') {
                    item.properties.isActiveOnPM = true;
                    item.properties.isActiveOnOP = true;
                    if (item.field === el || item.field === el + 'Status') {
                        item.properties.isRequiredOnPM = true;
                    }
                    return;
                }
                if (el === 'fixer' && ((item.field === 'fixStatus') || (item.field === 'fixStartTime') || (item.field === 'fixEndTime'))) {
                    item.properties.isActiveOnPM = true;
                    item.properties.isActiveOnOP = true;
                    if (item.field === 'fixStatus') {
                        item.properties.isRequiredOnPM = true;
                    }
                    return;
                }
            }
        });
       });
    }

    open(index: number): void {
        // open lightbox
        this._lightbox.open(this._albums, index);
    }
}
