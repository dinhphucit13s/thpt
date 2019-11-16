import { Injectable, Output, EventEmitter } from '@angular/core';
@Injectable()
export class EmitterService {
  private static _emitters: { [channel: string]: EventEmitter<any> } = {};
  static get(channel: string): EventEmitter<any> {
    if (!this._emitters[channel]) {
        this._emitters[channel] = new EventEmitter();
    }
    return this._emitters[channel];
  }
  static unsubscribe(channel: string): EventEmitter<any> {
    this._emitters[channel] = new EventEmitter();
    return this._emitters[channel];
  }
}
// INCLUDE EMITER: import { EmitterService } from '../../shared/services/emitter.service';
// -----DECLARE EVENT------
// EmitterService.get("ClickCollapedSlideBar").subscribe((data: any) => {
//  this.collapedSideBar = data;
// });
// -----EMIT EVENT------
// EmitterService.get("ClickCollapedSlideBar").emit(this.collapsed);
