import { Compiler, Component, NgModule, OnInit, AfterViewInit, Input, Output, EventEmitter, ViewChild, ViewChildren, ViewContainerRef, QueryList, ElementRef ,
    ComponentFactoryResolver, ComponentFactory} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Item } from '../services/item.model';
import {ProjectWorkflows} from '../../entities/project-workflows';

@Component({
  selector: 'jhi-workflow-activity',
  templateUrl: './workflow-activity.component.html',
  styles: []
})
export class WorkflowActivityComponent implements OnInit {
  @Input() workflowObject: ProjectWorkflows;
  @Output() detectChange: EventEmitter<Item>;
  @ViewChild('container', { read: ViewContainerRef }) container: ViewContainerRef;
  removeComponentClick: EventEmitter<Item> = new EventEmitter();
  // textComponentFactory: ComponentFactory<ActivityComponent>;
  activityItems: Item[] = new Array<Item>();
  items: Item[] = new Array<Item>();
  counterChildComponent = 1;
  constructor( private compiler: Compiler, private componentFactoryResolver: ComponentFactoryResolver
  ) {
    this.detectChange = new EventEmitter<Item>();
  }

  ngOnInit() {
      this.items = this.workflowObject.entityDTO;
      this.getActivityFields();
  }

  private getActivityFields() {
    this.items.forEach((value) => {
        if (value.properties.isActivityField) {
            this.activityItems.push(value);
        }
    });
  }
  onAddComponentClick() {
      this.workflowObject.activity.push('');
  }

  onRemoveComponentClick(index) {
      this.workflowObject.activity.splice(index, 1);
  }
}
