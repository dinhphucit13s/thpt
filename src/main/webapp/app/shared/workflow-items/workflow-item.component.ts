import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Item } from '../services/item.model';

@Component({
  selector: 'jhi-workflow-item',
  templateUrl: './workflow-item.component.html',
  styles: []
})
export class WorkflowItemComponent implements OnInit {
  @Input() workflowItem: Item;
  @Output() detectChange: EventEmitter<Item>;

  constructor(
  ) {
    this.workflowItem = new Item();

    this.detectChange = new EventEmitter<Item>();
  }

  onViewChange(position: string, item: string, value: boolean) {
  }

  ngOnInit() {
  }
}
