import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { Router, NavigationEnd, NavigationStart } from '@angular/router';
import { EmitterService } from '../../shared/services/emitter.service';
import { JhiLanguageHelper, Account, Principal, LoginModalService, LoginService } from '../../shared';
import { ProfileService } from '../profiles/profile.service';
import { DataService } from '../../shared/services/data.service';
import { Permission } from '../../account/login/permission.model';
import { PermissionService } from '../../account/login/permission.service';
import { HttpResponse } from '@angular/common/http';
declare var $: any;

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  providers: [PermissionService]
})
export class SidebarComponent implements OnInit {
  account: any;
  isActive = true;
  swaggerEnabled: boolean;
  inProduction: boolean;
  allPermission: Permission;
  adminResource: number;
  bussinessLine: number;
  businessUnit: number;
  businessUnitManager: number;
  projectTemplates: number;
  customer: number;
  bugListDefault: number;
  projects: number;
  purchaseOrders: number;
  membersManagement: number;
  allocation: number;
  effort: number;
  trackingManagement: number;
  notificationTemplate: number;
  tmsCustomerField: number;
  tmsCustomField: number;
  issue: number;
  questionAndAnswer: number;
  feedBack: number;
  viewFeedBack: number;
  tmsCustomerHistory: number;
  notes: number;
  currURL  = '';
  showMenu = '';
  pushRightClass = 'push-right';
  isAdmin = false;

  constructor(public router: Router, private profileService: ProfileService, private elRef: ElementRef
    , private principal: Principal, private data: DataService, private permissionService: PermissionService, ) {
    router.events.forEach((event) => {
      if (event instanceof NavigationStart) {
        this.currURL = event.url;
        this.activeMasterRoute();
      }
    });
   }
   activeMasterRoute() {
    const list = $(this.elRef.nativeElement).find('.page-sidebar-menu > li.nav-item');
    list.removeClass('active');
    for (let i = 0; i < list.length; i++) {
      const url = $(list[i]).find('a').attr('routerLink');
      if (url !== undefined) {
        if (this.currURL === url) {
          $(list[i]).addClass('active');
        } else {
          const intdexValue = this.currURL.indexOf(url);
          if (intdexValue < 2 && intdexValue > 0) {
            $(list[i]).addClass('active');
          }
        }
      }
    }
  }
  fHandResize() {
    const intHeight = $('.page-sidebar').height();
    let intHeightSet = 0;
    if (intHeight > window.innerHeight) {
      intHeightSet = intHeight;
    }else {
      intHeightSet = window.innerHeight - 50;
    }
    $('.page-content-wrapper > .page-content').css({ 'min-height': intHeightSet + 'px' });
  }
  fBindHandleSliderbar() {
    $(window).resize(() => {
      this.fHandResize();
    });
    $('.page-sidebar').find('a').click(() => {
      this.fHandResize();
    });
  }
  ngOnInit() {

    this.fBindHandleSliderbar();
    this.router.events.subscribe((val) => {
      if (
        val instanceof NavigationEnd &&
        window.innerWidth <= 992 &&
        this.isToggled()
      ) {
        this.toggleSidebar();
      }
    });

    this.profileService.getProfileInfo().then((profileInfo) => {
      this.inProduction = profileInfo.inProduction;
      this.swaggerEnabled = profileInfo.swaggerEnabled;
    });

    this.permissionService.findPermission()
      .subscribe((roleResponse: HttpResponse<Permission>) => {
        const role: Permission = roleResponse.body;
        this.data.changeUserPermission(role);
        this.data.currentUserPermission.subscribe((userPermission) => this.allPermission = userPermission);
        for (const resource of this.allPermission.resources) {
          console.log(resource.name, resource.permission);
          if (resource.name === 'ADMIN_RESOURCE') {
            this.adminResource = resource.permission;
          } else if (resource.name === 'BUSSINESS_LINE') {
            this.bussinessLine = resource.permission;
          } else if (resource.name === 'BUSINESS_UNIT') {
              this.businessUnit = resource.permission;
          } else if (resource.name === 'BUSINESS_UNIT_MANAGER') {
              this.businessUnitManager = resource.permission;
          }else if (resource.name === 'PROJECT_TEMPLATES') {
            this.projectTemplates = resource.permission;
          } else if (resource.name === 'PROJECT') {
            this.projects = resource.permission;
          } else if (resource.name === 'PURCHASE_ORDERS') {
            this.purchaseOrders = resource.permission;
          }else if (resource.name === 'MEMBERS_MANAGEMENT') {
              this.membersManagement = resource.permission;
          }else if (resource.name === 'TRACKING_MANAGEMENT') {
              this.trackingManagement = resource.permission;
          }else if (resource.name === 'ALLOCATION') {
              this.allocation = resource.permission;
          }else if (resource.name === 'EFFORT') {
              this.effort = resource.permission;
          }else if (resource.name === 'NOTIFICATION_TEMPLATE') {
              this.notificationTemplate = resource.permission;
          }else if (resource.name === 'TMS_CUSTOMER_FIELD') {
            this.tmsCustomerField = resource.permission;
          } else if (resource.name === 'ISSUE') {
            this.issue = resource.permission;
          }else if (resource.name === 'FEED_BACK') {
              this.feedBack = resource.permission;
          }else if (resource.name === 'VIEW_FEED_BACK') {
              this.viewFeedBack = resource.permission;
          } else if (resource.name === 'TMS_LOG_HISTORY') {
            this.tmsCustomerHistory = resource.permission;
          } else if (resource.name === 'NOTES') {
            this.notes = resource.permission;
          } else if (resource.name === 'BUG_LIST_DEFAULT') {
              this.bugListDefault = resource.permission;
          } else if (resource.name === 'CUSTOMER') {
              this.customer = resource.permission;
          }
        }
      });
  }
  // onResized(event: ResizedEvent): void {
  //   EmitterService.get('ResizeSlideBar').emit(event);
  // }

  eventCalled() {
    this.isActive = !this.isActive;
  }

  addExpandClass(element: any) {
    if (element === this.showMenu) {
      this.showMenu = '0';
    } else {
      this.showMenu = element;
    }
  }
  isToggled(): boolean {
    const dom: Element = document.querySelector('body');
    return dom.classList.contains(this.pushRightClass);
  }

  toggleSidebar() {
    const dom: any = document.querySelector('body');
    dom.classList.toggle(this.pushRightClass);
  }

  rltAndLtr() {
    const dom: any = document.querySelector('body');
    dom.classList.toggle('rtl');
  }
  onLoggedout() {
    localStorage.removeItem('isLoggedin');
  }
}
