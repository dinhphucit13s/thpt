import {Tasks} from '../tasks.model';
import {Component, Input, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {Bugs, BugsService} from '../../bugs';
import {Router} from '@angular/router';

@Component({
    selector: 'jhi-tasks-detail-reviewer',
    templateUrl: './tasks-detail-reviewer.component.html',
    styleUrls: ['./tasks-detail-reviewer.component.css']
})
export class TasksDetailReviewerComponent implements OnInit {
    @Input() tasks: Tasks;

    tasksId: any;
    bugsInfo: any[];
    hasBugsReview1: any;
    hasBugsReview2: any;
    hasBugsFI: any;
    currentUserLogin: any;
    constructor(private bugService: BugsService, private router: Router) {
    }
    ngOnInit() {
        console.log(this.tasks);
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.currentUserLogin = currentUser.login;
        this.tasksId = this.tasks.id;
        this.getBugsOfTasks(this.tasksId);
    }

    getBugsOfTasks(tasksId: number) {
        this.bugService.findBugsByTasksId(tasksId)
            .subscribe((res: HttpResponse<Bugs[]>) => {
                console.log(res.body);
                this.bugsInfo = res.body;
                this.hasBugsReview1 = this.bugsInfo.findIndex((bug) => bug.stage === 'review1');
                this.hasBugsReview2 = this.bugsInfo.findIndex((bug) => bug.stage === 'review2');
                this.hasBugsFI = this.bugsInfo.findIndex((bug) => bug.stage === 'fi');

                this.bugsInfo.forEach((bug) => {
                    bug.mediaType = {
                        isToggleComment: false,
                        isComment: false
                    };
                });
            });
    }

    previousState() {
        window.history.back();
    }
}
