import {Component, Input, OnInit} from '@angular/core';
import {Bugs, BugsService} from '../../../bugs';
import {UserService} from '../../../../shared';
import {JhiAlertService} from 'ng-jhipster';
import {Ng2ImgMaxService} from 'ng2-img-max';
import {Notes, NotesService} from '../../../notes';
import {MatDialog} from '@angular/material';
import {TasksDetailShowAttachmentComponent} from '../../tasks-detail-show-attachment/tasks-detail-show-attachment.component';

@Component({
    selector: 'jhi-tasks-detail-reviewer-content',
    templateUrl: './tasks-detail-reviewer-content.component.html',
    styleUrls: ['./tasks-detail-reviewer-content.component.css']
})

export class TasksDetailReviewerContentComponent implements OnInit {
    @Input() header: any;
    @Input() round: any;
    @Input() bugsInfo: any[];
    @Input() currentUserLogin: any;
    constructor(private  bugService: BugsService, private userService: UserService, private alertService: JhiAlertService,
                private ng2ImgMax: Ng2ImgMaxService, private notesService: NotesService, private dialog: MatDialog) {
    }
    ngOnInit() {
        console.log(this.round);
        console.log(this.bugsInfo);
        console.log(this.currentUserLogin);

    }

    showPanelComment(bug: Bugs) {
        if (!bug.mediaType.isComment) {
            bug.mediaType.isComment = true;
        }
    }

    toggleComment(bug: Bugs) {
        if (bug.mediaType.isToggleComment) {
            bug.mediaType.isToggleComment = false;
        } else {
            bug.mediaType.isToggleComment = true;
        }
    }

    getAndShowAttachment(bug: Bugs) {
        this.showAttachment(bug);
        if (bug.attachments.findIndex((x) => x.value === null) >= 0) {
            this.bugService.getAttachmentByBugId(bug.id).subscribe((res) => {
                bug.attachments = res.body;
            });
        }
    }

    showAttachment(bug: Bugs) {
        this.dialog.open(TasksDetailShowAttachmentComponent, {
            data: { model: bug , mode: 'edit', positionTop: window.scrollY }
        });
    }

    getAndShowAttachmentOfNotes(note: Notes) {
        this.showAttachmentNotes(note);
        if (note.attachments.findIndex((x) => x.value === null) >= 0) {
            this.notesService.getAttachmentByNotesId(note.id).subscribe((res) => {
                note.attachments = res.body;
            });
        }
    }

    showAttachmentNotes(notes: Notes) {
        this.dialog.open(TasksDetailShowAttachmentComponent, {
            data: { model : notes, mode: 'show', positionTop: window.scrollY}
        });
    }

    saveComment(bug: Bugs) {
        if (!bug.mediaType.commentAdd || bug.mediaType.commentAdd.replace(/ /g, '') === '') {
            return;
        }
        const notes: Notes = {
            bugId: bug.id,
            description: bug.mediaType.commentAdd,
            attachments: bug.mediaType.attachments
        };
        console.log(notes);
        const formData: FormData = new FormData();
        formData.append('notes', new Blob([JSON.stringify(notes)], {
            type: 'application/json'
        }));
        this.notesService.createNotesOfBugs(formData).subscribe((res) => {
            const noteAppend = res.body;
            let noteArray: Notes[];
            if (!bug.notes) {
                noteArray = new Array();
            } else {
                noteArray = bug.notes;
            }
            noteArray.push(noteAppend);
            bug.notes = noteArray;
            bug.mediaType.commentAdd = null;
            bug.mediaType.isToggleComment = true;
            bug.mediaType.isComment = false;
            if (bug.mediaType.isNotExistRequireNotes) {
                delete bug.mediaType.isNotExistRequireNotes;
            }
        }, (error) => {
            this.alertService.error(error);
        });
    }

    onFileChange(event, bug: Bugs) {
        console.log(event.target.files);
        let listFiles: any[];
        if (!bug.mediaType.attachments) {
            listFiles = new Array();
        } else {
            listFiles = bug.mediaType.attachments;
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
                    listFiles.push(fileReader);
                };
            }
        }
        bug.mediaType.attachments = listFiles;
        console.log(this.bugsInfo);
    }

    formatDate(date) {
        return new Date(date).toLocaleString();
    }
}
