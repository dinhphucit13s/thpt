import {Component, Input, Output, OnInit, EventEmitter, AfterViewInit, OnChanges} from '@angular/core';
import {GridOptions} from 'ag-grid';
import {Account, Principal} from '../index';
import {Router} from '@angular/router';
declare var jquery: any;
declare var $: any;

@Component({
    selector: 'jhi-data-collection',
    templateUrl: './data-table.component.html',
})
export class DataCollectionComponent implements OnInit, AfterViewInit, OnChanges {
    /*ar-grid*/
    @Input() _gridOptions;
    @Input() _columnDef;
    @Input() _rowData;
    @Input() _rowSelected;
    @Input() _frameworkComponents;
    @Input() permission;
    /*params pagination*/
    @Input() _hiddenPage;
    @Input() page;
    @Input() queryCount;
    @Input() itemsPerPage;
    @Input() totalItems;
    @Input() toolPanel;
    @Input() enableSave;
    @Output() loadPage = new EventEmitter();
    @Output() loadAuthorities = new EventEmitter();
    @Output() loadCustomReport = new EventEmitter();

    @Input() project;
    @Input() _context;
    @Input() isRowSelectable;
    /*custom report field*/
    @Input() dataCustomReport;

    frameworkComponents: any;
    gridOptions: GridOptions;
    hiddenPage: boolean;
    firstLoadComponent = true;
    sideBar;
    hasContent = false;
    gridApi;
    gridColumnApi;
    columnDefArg: any = new Array();
    columnDefArgTest: any;
    subColumnDefArg: any = new Array();
    columnDefCustom: any = new Array();
    hasConfigField = 0;
    customFieldReport: any = new Array();
    testList = new Array();
    account: Account;
    review2Hide: any;
    fiHide: any;
    fixerHide: any;
    dataTask = ['data', 'file_name', 'type', 'availability', 'frame', 'actual_object', 'duration', 'target', 'error_quantity', 'error_severity', 'parent'];
    dataHide = true;
    constructor(
        private principal: Principal,
        private router: Router) {
    }
    onSelectionChanged(param?: any) {
        const selectedRows = this.gridApi.getSelectedRows();
        this.loadAuthorities.emit(selectedRows);
    }
    emitPage(value: number) {
        this.loadPage.emit(this.page);
    }
    emitCustomReport() {
        this.loadCustomReport.emit(this.customFieldReport);
    }
    ngOnChanges() {
        if (this._columnDef !== undefined) {
            this.columnDefArg = new Array();
            this.onGridColumnsChanged();
        }

        if (this.gridOptions !== undefined) {
            this.review2Hide = true;
            this.fiHide = true;
            this.fixerHide = true;
            this.toogleByFixer(this._columnDef);
            this.gridOptions.api.setColumnDefs(this._columnDef);
        }
    }

    toogleByFixer(listColumns?: any) {
        if (this.dataCustomReport && this.dataCustomReport.length > 0) {
            this.customFieldReport = this.dataCustomReport;
            this.dataCustomReport.forEach((data) => {
                if (data === 'Review2') {
                    this.review2Hide = false;
                } else if (data === 'Fi') {
                    this.fiHide = false;
                } else if (data === 'Fixer') {
                    this.fixerHide = false;
                }
            });
            this.toogleCustomReport();
        }
    }
    toogleCustomReport() {
        if (this._columnDef !== undefined) {
            this._columnDef.forEach((col) => {
                if (col.headerName === 'Review2') {
                    col.showToogle = this.review2Hide;
                } else if (col.headerName === 'Fi') {
                    col.showToogle = this.fiHide;
                } else if (col.headerName === 'Fixer') {
                    col.showToogle = this.fixerHide;
                } else if (col.headerName.includes('Review2_')) {
                    col.children.forEach((child) => {
                        child.hide = this.review2Hide;
                    });
                } else if (col.headerName.includes('Fi_')) {
                    col.children.forEach((child) => {
                        child.hide = this.fiHide;
                    });
                } else if (col.headerName.includes('Fixer_')) {
                    col.children.forEach((child) => {
                        child.hide = this.fixerHide;
                    });
                } else if (col.headerName === '') {
                    this.dataCustomReport.forEach((cus) => {
                       col.children.forEach((child) => {
                          if (child.headerName === cus) {
                              child.showToogle = false;
                              child.hide = child.showToogle;
                          }
                       });
                    });
                }
                    /*{
                    this.dataCustomReport.forEach((cus) => {
                        if (col.headerName === '') {
                                col.children.forEach((child) => {
                                    if (child.headerName === cus) {
                                        child.showToogle = false;
                                        child.hide = child.showToogle;
                                    }
                                });
                        }
                    });
                }*/
            });
            console.log(this._columnDef);
        }
    }
    ngOnInit() {
        // set pagination
        if (this._hiddenPage !== undefined) {
            this.hiddenPage = this._hiddenPage;
        } else {
            this.hiddenPage = false;
        }
        // set gridOptions
        if (this._gridOptions !== undefined) {
            this.gridOptions = this._gridOptions;
        } else {
            this.gridOptions = <GridOptions> {
                            rowSelection: 'multiple',
                            getSelectedRows: 'getSelectedRows',
                            suppressRowClickSelection: true,
                            domLayout: 'autoHeight',
                            rowDragManaged: true,
                            enableColResize: true,
                            defaultColDef: {
                                width: 150,
                            },
                            context: {
                                componentParent: this
                            },
            };
        }
        this.sideBar = 'columns';
    }

    ngAfterViewInit() {
        $('.toolPanelItem').click(function(){
            const toolPanelContent = $(this).closest('.grid-tool-panel').find('.toolPanelContent');
            const tool = $(this).parent();
            const offset = $(tool).position();
            $('.toolPanelContent').not($(toolPanelContent)).css('top',  '-500000px').removeClass('selected');
            if (!$(toolPanelContent).hasClass('selected')) {
                $(toolPanelContent).addClass('selected');
                $(toolPanelContent).css('top', offset.top + 'px');
                $(toolPanelContent).css('left', offset.left - 182 + 'px');
            }else {
                $(toolPanelContent).css('top', '-500000px').removeClass('selected');
            }
        });
    }
    toogleColumnContent() {
        if (!this.hasContent) {
            const offset = $('.tool').position();
            $('.toolPanelContent').css({'left': offset.left - 182 + 'px', 'top': offset.top + 'px'});
        }else {
            $('.toolPanelContent').css('top', '-500000px');
        }
        this.hasContent = !this.hasContent;
    }

    showDetailFilePath(params) {
        if (params.colDef.field === 'fileName') {
            if (params.value) {
                const elem = params.event.path[2];
                console.log($(elem).outerWidth());
                console.log($(elem).outerHeight());
                const left = params.event.clientX - params.event.offsetX + ($(elem).outerWidth() * 20 / 100) + window.scrollX;
                const top = params.event.clientY - params.event.offsetY + $(elem).outerHeight() + window.scrollY + 10;
                const value = params.value.replace(/___/g, '$');
                const valueArr = value.split('$');
                let inside = '';
                valueArr.forEach((val) => {
                    inside = inside + '<div><a class="tooltip-path">'
                        + '<span class="tooltiptext">Copy to clipboard</span>Copy</a><span>' + val + '</span></div>';
                });
                const html = '<div class="detail-path" style="position: absolute; top:'
                    + top + 'px' + '; left: ' + left + 'px' + '"><span>' + inside + '</span></div>';
                $('body').find('.detail-path').remove();
                $('body').append(html);
                $('body').find('.detail-path').find('a').click(function(event) {
                    const text = $(event.target).next().text();
                    const selBox = document.createElement('textarea');
                    selBox.style.position = 'fixed';
                    selBox.style.left = '0';
                    selBox.style.top = '0';
                    selBox.style.opacity = '0';
                    selBox.value = text;
                    document.body.appendChild(selBox);
                    selBox.focus();
                    selBox.select();
                    document.execCommand('copy');
                    document.body.removeChild(selBox);
                    $('.detail-path').find('a').find('span').text('Copy to clipboard');
                    $(event.target).find('span').text('Copied');
                });
                $('body').click(
                    function() {
                        $('body').find('.detail-path').remove();
                    });
                $('.detail-path').click(function(event) {
                    event.stopPropagation();
                });
            }
        }
    }

    toogleGridColum(col) {
        if (col.showToogle !== undefined) {
            col.hide = col.showToogle;
            col.showToogle = !col.showToogle;
        }
        this.gridColumnApi.setColumnVisible(col.field, col.hide);
        col.hide = !col.hide;
        this.toogleColumnsChildCustomReport(col);
        const index = this.customFieldReport.findIndex((field) => field === col.headerName);
        if (index >= 0) {
            this.customFieldReport.splice(index, 1);
        } else {
            this.customFieldReport.push(col.headerName);
        }
        this.gridApi.setColumnDefs(this._columnDef);
    }
    toogleColumnsChildCustomReport(colChild) {
        this._columnDef.forEach((col) => {
            const nameColumn = colChild.headerName + '_';
           if (col.children !== undefined && col.headerName.includes(nameColumn)) {
               col.children.forEach((child) => {
                   this.gridColumnApi.setColumnVisible(child.field, child.hide);
                   child.hide = !child.hide;
                   this.gridApi.setColumnDefs(this._columnDef);
               });
           }
        });
    }
    onGridReady(params) {
        this.gridApi = params.api;
        this.gridColumnApi = params.columnApi;
        params.api.sizeColumnsToFit();
        params.api.forEachNode( (node) => {
            const temp = node.data;
            if (this._rowSelected !== null && this._rowSelected !== undefined) {
                this._rowSelected.forEach((row) => {
                    if (temp.name === row) {
                        node.setSelected(true);
                    }
                });
            }
        });
    }
    onGridColumnsChanged(params?: any) {
        $('.ag-sort-order').css('display', 'none');
        if (this._columnDef !== undefined) {
            this._columnDef.forEach((item) => {
                if (item.hide) {
                    this.columnDefArg.push(true);
                    this.hasConfigField += 1;
                } else {
                    this.columnDefArg.push(false);
                }
            });
            if (this.hasConfigField === 0 && this._columnDef.length > 0) {
                this.toolPanel = false;
            }
        }
        if (params) {
            const column = params.columnApi.getColumn('id');
            if (column) {
                setTimeout(function() {
                    let maxWidthAction = 0;
                    const $mainCollection = $('jhi-data-collection:visible');
                    let $btnPnlElem = $mainCollection.find('jhi-button-view').children();
                    if ($btnPnlElem.length === 0) {
                        $btnPnlElem = $mainCollection.find('jhi-button-view-ds10').children();
                    }
                    if ($btnPnlElem.length > 0) {
                        $btnPnlElem.each(function(index, value) {
                            let sumWidthActions = 0;
                            $(value).children().each(function(i, val) {
                                sumWidthActions += $(val).outerWidth();
                            });
                            if (maxWidthAction < sumWidthActions) {
                                maxWidthAction = sumWidthActions;
                            }
                        });
                        console.log(maxWidthAction);

                        params.columnApi.setColumnWidth(column , maxWidthAction + 22, true);
                    }
                }, 300);
            }
        }
    }

    onBodyScroll(params?: any) {
        $('.ag-sort-order').css('display', 'none');
    }
}
