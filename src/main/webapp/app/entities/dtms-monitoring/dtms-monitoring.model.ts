import { BaseEntity } from './../../shared';

export const enum PositionMonitoring {
    'PROJECT',
    ' PURCHASE_ORDER'
}

export const enum MONITORINGROLE {
    'ROLE_DEDICATED',
    'ROLE_WATCHER'
}

export class DtmsMonitoring implements BaseEntity {
    constructor(
        public id?: number,
        public position?: PositionMonitoring,
        public positionId?: number,
        public role?: MONITORINGROLE,
        public members?: string,
    ) {
    }
}
