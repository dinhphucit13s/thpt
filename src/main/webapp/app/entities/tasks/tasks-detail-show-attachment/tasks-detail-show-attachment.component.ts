import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Lightbox} from 'ngx-lightbox';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
    selector: 'jhi-tasks-detail-show-attachment',
    templateUrl: './tasks-detail-show-attachment.component.html',
    styleUrls: ['./tasks-detail-show-attachment.component.css']
})
export class TasksDetailShowAttachmentComponent implements OnInit {
    constructor(public dialogRef: MatDialogRef<TasksDetailShowAttachmentComponent>,
                @Inject(MAT_DIALOG_DATA) public data: any, private _lightbox: Lightbox,
                private sanitizer: DomSanitizer) {}
    ngOnInit() {

    }

    sanitize(url: string) {
        return this.sanitizer.bypassSecurityTrustUrl(url);
    }

    open(attachments, index): void {
        // open lightbox
        let albums: any[] = new Array();
        attachments.forEach((attachment) => {
            albums = [
                ...albums,
                {
                    src: 'data:image/png' + ';base64,' + attachment.value
                }
            ];
        });
        this._lightbox.open(albums, index, {positionFromTop: this.data.positionTop + 20});
    }
}
