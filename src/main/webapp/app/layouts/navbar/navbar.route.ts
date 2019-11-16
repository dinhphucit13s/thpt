import { Route } from '@angular/router';

import { NavbarComponent } from './navbar.component';
import { UserRouteAccessService } from '../../shared';
export const navbarRoute: Route = {
    path: '',
    component: NavbarComponent,
    outlet: 'navbar',
    canActivate: [UserRouteAccessService],
};
