import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Lightbox} from 'ngx-lightbox';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
    selector: 'jhi-mail-show-attachment',
    templateUrl: './mail-show-attachment.component.html',
    styleUrls: ['./mail-show-attachment.component.css']
})

export class MailShowAttachmentComponent implements OnInit {
    constructor(public dialogRef: MatDialogRef<MailShowAttachmentComponent>,
                @Inject(MAT_DIALOG_DATA) public data: any, private _lightbox: Lightbox,
                private sanitizer: DomSanitizer) {
    }

    ngOnInit() {

    }

    removeAttachment(attach) {
        const objX = this.data.model.filter((x) => x !== attach);
        this.data.model = objX;

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
}
