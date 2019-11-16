import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, ActivatedRoute } from '@angular/router';
import { EmitterService } from '../../shared/services/emitter.service';
import { JhiLanguageHelper, Principal } from '../../shared';
@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.css'],
    host: {
        '(window:resize)': 'onResize($event)'
    }
})
export class JhiMainComponent implements OnInit {
    collapedSideBar: boolean;
    private router: Router;
    private account: any;
    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        private _router: Router,
        private principal: Principal,
        private activeRoute: ActivatedRoute
    ) {
        this.router = _router;

    }
    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = (routeSnapshot.data && routeSnapshot.data['pageTitle']) ? routeSnapshot.data['pageTitle'] : 'akaRpaApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }
    onResize(event) {
        const el: any = document.getElementsByClassName('page-content');
        if (el.length > 0) {
            el[0].style.minHeight = (event.target.innerHeight - 50) + 'px';
        }
    }
    isAuthenticated() {
        return this.principal.isAuthenticated();
    }
    ngOnInit() {
        EmitterService.get('ResizeSlideBar').subscribe((data: any) => {
            const el: any = document.getElementsByClassName('page-content');
            if (el.length > 0) {
                let intHeight = window.innerHeight - 50;
                if (data.newHeight > intHeight) {
                    intHeight = data.newHeight;
                }
                el[0].style.minHeight = intHeight + 'px';
            }
        });
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });

    }
}
