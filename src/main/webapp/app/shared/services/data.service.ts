import { BehaviorSubject } from 'rxjs';
import { Item } from './item.model';
import { AppConstants} from './app-constants';
import { RolePermission } from '../../entities/authority-resource/role-permission.model';
import { Role } from '../../entities/authority-resource/role.model';
import { Permission } from '../../account/login/permission.model';
import {ReuseSelect} from '../dynamic-forms/select/select.model';

export class DataService {
  defaultPurchaseOrderItems = AppConstants.PurchaseOrderItems;
  defaultPackageItems = AppConstants.PackageItems;
  defaultTaskItems = AppConstants.TaskItems;

  private orderItems = new BehaviorSubject(new Array<Item>());
  private pmItems = new BehaviorSubject(new Array<string>());
  private opItems = new BehaviorSubject(new Array<string>());
  private packageItems = new BehaviorSubject(new Array<Item>());
  private taskItems = new BehaviorSubject(new Array<Item>());

  private editOrderItems = new BehaviorSubject(new Array<Item>());
  private editPackageItems = new BehaviorSubject(new Array<Item>());
  private editTaskItems = new BehaviorSubject(new Array<Item>());
  private reusedData = new BehaviorSubject(new Array<ReuseSelect>());

  // Initialize role data
  private rolePermissions = new BehaviorSubject(new Array<RolePermission>());
  private roleSavingMode = new BehaviorSubject('create');
  private userPermission = new BehaviorSubject(new Permission());
  private permission = new BehaviorSubject(0);

  currentPurchaseOrderItems = this.orderItems.asObservable();
  currentPackageItems = this.packageItems.asObservable();
  currentTaskItems = this.taskItems.asObservable();

  currentEditPurchaseOrderItems = this.editOrderItems.asObservable();
  currentEditPackageItems = this.editPackageItems.asObservable();
  currentEditTaskItems = this.editTaskItems.asObservable();

  currentPMItems = this.pmItems.asObservable();
  currentOPItems = this.opItems.asObservable();

  currentRolePermissions = this.rolePermissions.asObservable();
  currentRoleSavingMode = this.roleSavingMode.asObservable();
  currentUserPermission = this.userPermission.asObservable();
  currentPermission = this.permission.asObservable();
  reusedDataSub = this.reusedData.asObservable();
  constructor() {
      this.orderItems.next(this.defaultPurchaseOrderItems);
      this.packageItems.next(this.defaultPackageItems);
      this.taskItems.next(this.defaultTaskItems);
  }

  changeReusedData(data: Array<ReuseSelect>) {
      this.reusedData.next(data);
  }

  getReusedData() {
      return this.reusedData;
  }

  changeOrderItem(items: Array<Item>) {
    this.orderItems.next(items);
  }

  changeEditOrderItem(items: Array<Item>) {
    this.editOrderItems.next(items);
  }

  changePackageItem(items: Array<Item>) {
    this.packageItems.next(items);
  }

  changeEditPackageItem(items: Array<Item>) {
    this.editPackageItems.next(items);
  }

  changeTaskItem(items: Array<Item>) {
    this.taskItems.next(items);
  }

  changeEditTaskItem(items: Array<Item>) {
    this.editTaskItems.next(items);
  }

  changeOPItem(items: Array<string>) {
    this.opItems.next(items);
  }

  changePMItem(items: Array<string>) {
    this.pmItems.next(items);
  }

  changeRolePermissions(role: RolePermission[]) {
    this.rolePermissions.next(role);
  }

  changeRoleSavingMode(mode: string) {
    this.roleSavingMode.next(mode);
  }

  changeUserPermission(role: Permission) {
    this.userPermission.next(role);
  }

  setPermission(permission: any) {
      this.permission.next(permission);
  }

  resetRolePermission() {
    this.rolePermissions.next(new Array<RolePermission>());
  }

  resetRoleSavingMode() {
    this.roleSavingMode.next('CREATE');
  }

  resetUserPermission() {
    this.userPermission.next(new Permission());
  }

  resetItems() {
    this.orderItems.next(this.defaultPurchaseOrderItems);
    this.packageItems.next(this.defaultPackageItems);
    this.taskItems.next(this.defaultTaskItems);
    this.opItems.next(new Array<string>());
    this.pmItems.next(new Array<string>());
  }
}
