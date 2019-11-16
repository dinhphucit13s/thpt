
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import * as alertify from 'alertifyjs';
@Injectable()
export class NotificationService {
  private _notifier: any = alertify;
  constructor(private translate: TranslateService) {
    alertify.defaults = {
      // dialogs defaults
      autoReset: true,
      basic: false,
      closable: true,
      closableByDimmer: true,
      frameless: false,
      maintainFocus: true, // <== global default not per instance, applies to all dialogs
      maximizable: true,
      modal: true,
      movable: true,
      moveBounded: false,
      overflow: true,
      padding: true,
      pinnable: true,
      pinned: true,
      preventBodyShift: false, // <== global default not per instance, applies to all dialogs
      resizable: true,
      startMaximized: false,
      transition: 'pulse',

      // notifier defaults
      notifier: {
        // auto-dismiss wait time (in seconds)
        delay: 5,
        // default position
        position: 'top-right',
        // adds a close button to notifier messages
        closeButton: false
      },

      // language resources
      glossary: {
        // dialogs default title
        title: 'Xác nhận',
        // ok button text
        ok: 'Đồng ý',
        // cancel button text
        cancel: 'Hủy'
      },

      // theme settings
      theme: {
        // class name attached to prompt dialog input textbox.
        input: 'ajs-input',
        // class name attached to ok button
        ok: 'ajs-ok',
        // class name attached to cancel button
        cancel: 'ajs-cancel'
      }
    };

  }
  success(message: string) {
    this._notifier.success(message);
  }
  error(message: string) {
    this._notifier.error(message);
  }
  warning(message: string) {
    this._notifier.warning(message);
  }
  message(message: string) {
    this._notifier.message(message);
  }
  confirm(message: string, okCallback: () => any) {
    this._notifier.defaults.glossary.title = this.translate.instant('global.messages.confirm');
    this._notifier.defaults.glossary.ok = this.translate.instant('entity.action.ok');
    this._notifier.defaults.glossary.cancel = this.translate.instant('entity.action.cancel');
    this._notifier.confirm(message, function(e) {
      if (e) {
        okCallback();
      }
    });
  }
}
