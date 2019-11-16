import {Properties} from '../services/properties.model';
import {Validators} from '@angular/forms';
import construct = Reflect.construct;
import {TMSCustomFieldScreen} from '../../entities/tms-custom-field-screen/tms-custom-field-screen.model';
export class Validator {
    constructor(
        public name?: any,
        public validator?: any,
        public message?: any
    ) {}
}
export interface RouterPages {
    routerView: string;
    routerED: string;
}

export interface OptionsValue {
    id: any;
    name: any;
}

export class DynamicPropertiesField {
    constructor(
        public entityPropertiesData?: FieldDataConfig,
        public entityPropertiesView?: FieldPropertiesConfig
    ) {
        this.entityPropertiesData = entityPropertiesData ? entityPropertiesData : new FieldDataConfig();
        this.entityPropertiesView = entityPropertiesView ? entityPropertiesView : new FieldPropertiesConfig();
    }
}

export class DynamicFieldConfig {
    constructor(
        public fieldConfigVMs?: FieldConfig[],
        public tmsCustomFieldScreenDTOs?: TMSCustomFieldScreen[]
    ) {
        this.fieldConfigVMs = fieldConfigVMs ? fieldConfigVMs : new Array<FieldConfig>();
        this.tmsCustomFieldScreenDTOs = tmsCustomFieldScreenDTOs ? tmsCustomFieldScreenDTOs : new Array<TMSCustomFieldScreen>();
    }
}

export class FieldConfig {
    constructor(
        public position?: string,
        public inputType?: string,
        public options?: any,
        public urlOption?: string,
        public paramOptions?: any,
        public collections?: any,
        public type?: string,
        public value?: any,
        public properties?: Properties,
        public validations?: Validator[],
        public headerName?: string,
        public children?: any,
        public field?: string,
        public width?: number,
        public valueGetter?: any,
        public cellRenderer?: any,
        public pinned?: string,
        public format?: any,
        public colId?: string,
        public cellRendererParams?: any,
        public cellEditor?: any,
        public checkboxSelection?: any,
        public isSelected?: any,
        public headerCheckboxSelection?: any,
        public suppressRowClickSelection?: any,
        public rowSelection?: any,
        public editable?: boolean,
        public filter?: string,
        public enableFilter?: boolean,
        public reuseData?: any,
        public hide?: boolean,
        public hideDynamicField?: boolean,
        public cellStyle?: any,
        public backgroundColor?: any,
        public color?: any,
        public sortable?: boolean,
        public sort?: string,
        public rowDrag?: boolean,
        public displayPosition?: number,
        public sortByPosition?: number,
        public rowStyle?: any,
    ) {
        // position of screen
        this.position = position ? position : null;
        // type of input tag
        this.inputType = inputType ? inputType : null;
        // value default of select tag
        this.options = options ? options : null;
        // API of field
        this.urlOption = urlOption ? urlOption : null;
        //
        this.paramOptions = paramOptions ? paramOptions : null;
        this.collections = collections ? collections : null;
        // type of input tag
        this.type = type ? type : null;
        // value default of field
        this.value = value ? value : null;
        // properties of field
        this.properties = properties ? properties : new Properties();
        // validations of field
        this.validations = validations ? validations : new Array<Validator>();
        // label of field
        this.headerName = headerName ? headerName : null;
        // group colunm
        this.children = children ? children : null;
        // name field value
        this.field = field ? field : field;
        // property width of field in view screen
        this.width = width ? width : 150;
        //
        this.valueGetter = valueGetter ? valueGetter : null;
        // set properties for field
        this.cellRenderer = cellRenderer ? cellRenderer : null;
        this.pinned = pinned ? pinned : null;
        // format field
        this.format = format ? format : null;
        // colId of field
        this.colId = colId ? colId : null;
        // param of cellRenderer
        this.cellRendererParams = cellRendererParams ? cellRendererParams : null;
        // edit cell
        this.cellEditor = cellEditor ? cellEditor : null;
        // checkbox select
        this.checkboxSelection = checkboxSelection ? checkboxSelection : false;
        // header checkbox
        this.headerCheckboxSelection = headerCheckboxSelection ? headerCheckboxSelection : false;
        // suppress
        this.suppressRowClickSelection = suppressRowClickSelection ? suppressRowClickSelection : null;
        // event select - single row selection (single) - multiple row selection (multiple)
        this.rowSelection = rowSelection ? rowSelection : null;
        // double click edit cell
        this.editable = editable ? editable : false;
        // filter column of field
        this.filter = filter ? filter : null;
        // enable filter
        this.enableFilter = enableFilter ? enableFilter : false;
        // use data old
        this.reuseData = reuseData ? reuseData : null;
        // hidden field on screen view
        this.hide = hide ? hide : false;
        // hidden field in form
        this.hideDynamicField = hideDynamicField ? hideDynamicField : false;
        // set backgroundColor for column
        this.backgroundColor = backgroundColor ? backgroundColor : null;
        // set color
        this.color = color ? color : null;
        // style of field
        this.cellStyle = cellStyle ? cellStyle : null;
        // sort
        this.sortable = sortable ? sortable : true;
        // sort type
        this.sort = sort ? sort : 'asc';
        // draggable area included in the cell (kéo tới vị trí khác).
        this.rowDrag = rowDrag ? rowDrag : true;
        // position show on group
        this.displayPosition = displayPosition ? displayPosition : 1;
        // position sort on group
        this.sortByPosition = sortByPosition ? sortByPosition : 1;
    }
}

export class FieldDataConfig {
    constructor(
        public position?: string,
        public inputType?: string,
        public options?: any,
        public urlOption?: string,
        public paramOptions?: any,
        public type?: string,
        public value?: any,
        public validations?: Validator[],
        public headerName?: string,
        public field?: string,
        public format?: any,
        public colId?: string,
        public reuseData?: any,
    ) {
        // position of screen
        this.position = position ? position : null;
        // type of input tag
        this.inputType = inputType ? inputType : null;
        // value default of select tag
        this.options = options ? options : null;
        // API of field
        this.urlOption = urlOption ? urlOption : null;
        //
        this.paramOptions = paramOptions ? paramOptions : null;
        // type of input tag
        this.type = type ? type : null;
        // value default of field
        this.value = value ? value : null;
        // validations of field
        this.validations = validations ? validations : new Array<Validator>();
        // label of field
        this.headerName = headerName ? headerName : headerName;
        // name field value
        this.field = field ? field : field;
        // format field
        this.format = format ? format : null;
        // colId of field
        this.colId = colId ? colId : null;
        // use data old
        this.reuseData = reuseData ? reuseData : null;
    }
}

export class FieldPropertiesConfig {
    constructor(
        public collections?: any,
        public properties?: Properties,
        public width?: number,
        public valueGetter?: any,
        public cellRenderer?: any,
        public pinned?: string,
        public cellRendererParams?: any,
        public cellEditor?: any,
        public checkboxSelection?: any,
        public isSelected?: any,
        public headerCheckboxSelection?: any,
        public suppressRowClickSelection?: any,
        public rowSelection?: any,
        public editable?: boolean,
        public filter?: string,
        public enableFilter?: boolean,
        public hide?: boolean,
        public hideDynamicField?: boolean,
        public cellStyle?: any,
        public backgroundColor?: any,
        public color?: any,
        public sortable?: boolean,
        public sort?: string,
        public rowDrag?: boolean,
        public displayPosition?: number,
        public sortByPosition?: number,
        public validations?: Validator[],
    ) {
        this.collections = collections ? collections : null;
        // properties of field
        this.properties = properties ? properties : new Properties();
        // property width of field in view screen
        this.width = width ? width : 150;
        //
        this.valueGetter = valueGetter ? valueGetter : null;
        // set properties for field
        this.cellRenderer = cellRenderer ? cellRenderer : null;
        this.pinned = pinned ? pinned : null;
        // param of cellRenderer
        this.cellRendererParams = cellRendererParams ? cellRendererParams : null;
        // edit cell
        this.cellEditor = cellEditor ? cellEditor : null;
        // checkbox select
        this.checkboxSelection = checkboxSelection ? checkboxSelection : false;
        // header checkbox
        this.headerCheckboxSelection = headerCheckboxSelection ? headerCheckboxSelection : false;
        // suppress
        this.suppressRowClickSelection = suppressRowClickSelection ? suppressRowClickSelection : null;
        // event select
        this.rowSelection = rowSelection ? rowSelection : null;
        // double click edit cell
        this.editable = editable ? editable : false;
        // filter column of field
        this.filter = filter ? filter : null;
        // enable filter
        this.enableFilter = enableFilter ? enableFilter : false;
        // hidden field on screen view
        this.hide = hide ? hide : false;
        // hidden field in form
        this.hideDynamicField = hideDynamicField ? hideDynamicField : false;
        // style of field
        this.cellStyle = cellStyle ? cellStyle : null;
        // set backgroundColor for column
        this.backgroundColor = backgroundColor ? backgroundColor : null;
        // set color
        this.color = color ? color : null;
        // sort
        this.sortable = sortable ? sortable : true;
        // sort type
        this.sort = sort ? sort : 'asc';
        this.rowDrag = rowDrag ? rowDrag : true;
        // position show on group
        this.displayPosition = displayPosition ? displayPosition : 1;
        // position sort on group
        this.sortByPosition = sortByPosition ? sortByPosition : 1;
        // validations of field
        this.validations = validations ? validations : new Array<Validator>();
    }
}
