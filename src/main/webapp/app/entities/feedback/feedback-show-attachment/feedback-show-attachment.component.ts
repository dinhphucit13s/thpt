import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Lightbox} from 'ngx-lightbox';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-feedback-show-attachment',
  templateUrl: './feedback-show-attachment.component.html',
  styleUrls: ['./feedback-show-attachment.component.css']
})

export class FeedbackShowAttachmentComponent implements OnInit {
  constructor(public dialogRef: MatDialogRef<FeedbackShowAttachmentComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any, private _lightbox: Lightbox,
              private sanitizer: DomSanitizer) {}

  ngOnInit() {
      let tam = this.data.model.attachments;
  }

  removeAttachment(attach) {
    const objX = this.data.model.attachments.filter(x => x !== attach);
    this.data.model.attachments = objX;

    if (!this.data.model.attachmentsRemove) {
      this.data.model.attachmentsRemove = new Array();
    }

    if (!this.data.model.attachmentsAppend) {
      this.data.model.attachmentsAppend = new Array();
    }
    const objRemove = this.data.model.attachmentsAppend.find(x => x === attach);

    if (!objRemove) {
      this.data.model.attachmentsRemove.push(attach);
    }
    const objAppendX  = this.data.model.attachmentsAppend.filter(x => x !== attach);
    this.data.model.attachmentsAppend = objAppendX;

    console.log(this.data.model);
  }

  sanitize(url: string) {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  open(attachments, index): void {
    // open lightbox
    let albums: any[] = new Array();
    attachments.forEach(attachment => {
      albums = [
        ...albums,
        {
          src: 'data:image/png' + ';base64,' + attachment.value
        }
      ];
    });
    this._lightbox.open(albums, index);
  }
}
