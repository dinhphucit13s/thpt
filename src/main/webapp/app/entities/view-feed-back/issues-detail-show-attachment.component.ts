import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import { Lightbox } from 'ngx-lightbox';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
    selector: 'jhi-issues-detail-show-attachment',
    templateUrl: './issues-detail-show-attachment.component.html',
})
export class IssuesDetailShowAttachmentComponent implements OnInit {
    isUpdate: boolean;
    currentUser: any;
    constructor(public dialogRef: MatDialogRef<IssuesDetailShowAttachmentComponent>,
                @Inject(MAT_DIALOG_DATA) public data: any, private _lightbox: Lightbox,
                private sanitizer: DomSanitizer) {}
    ngOnInit() {
        let tam = this.data.model.attachments;
        this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    }

    sanitize(url: string) {
        return this.sanitizer.bypassSecurityTrustUrl(url);
    }

    onFileChange(event, model: any) {
        console.log(event.target.files);
        console.log(model);
        if (!model.attachmentsAppend) {
            model.attachmentAppend = new Array();
        }
        if (!model.attachment) {
            model.attachment = new Array();
        }
        if (event.target.files && event.target.files.length) {
            for (let i = 0; i < event.target.files.length; i++) {
                const reader = new FileReader();
                const file = event.target.files[i];
                reader.readAsDataURL(file);
                reader.onloadend = () => {
                    const fileReader = {
                        filename: file.name,
                        fileType: file.type,
                        diskFile: '',
                        value: reader.result.split(',')[1],
                    };
                    model.attachment.push(fileReader);
                    model.attachmentAppend.push(fileReader);

                    if (this.data.model.code) {
                        this.data.model.mediaType.isEditUpdate = true;
                    }
                };
            }
        }
        console.log(model);
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
