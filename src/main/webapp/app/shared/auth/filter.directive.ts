import { Directive, ElementRef, AfterViewInit, Input } from '@angular/core';
require('../../../content/js/bootstrap-select.min.js');
declare var $: any;
@Directive({
    selector: 'fsoftFilter'
})
export class FilterDirective implements AfterViewInit {
    @Input() fieldName: string;
    @Input() label: string; // label
    @Input() type: string; // string||select chuyền vào kiểu hiển thị
    @Input() array: any; // chuyền vào nếu là dạng combobox select
    @Input() outPara: string; // tham số out ngược lại component

    @Input()
    public callback: Function;
    private ArrValue: string[];
    constructor(private elRef: ElementRef) {

    }
    GetNewID(): string {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            const r = Math.random() * 16 | 0, v = c === 'x' ? r : ( r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
    replaceAll(strIn: string, strFind: string, strReplace: string) {
        return strIn.replace(new RegExp(strFind.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&'), 'g'), strReplace);
    }
    bindOutPut() {
        let strReturnValue = '';
        const item: any = {};

        if (this.type === 'string') {
            strReturnValue = $(this.elRef.nativeElement).find('.popover input').val();
        }else if (this.type === 'select') {
            strReturnValue = $(this.elRef.nativeElement).find('.popover select').val();
        }
        eval('item.' + this.outPara + '={}');
        eval('item.' + this.outPara + '.' + this.fieldName + '="' + encodeURI(strReturnValue) + '"');
        this.callback(item);
    }
    ngAfterViewInit(): void {
        if (this.array !== undefined) {
            this.array = this.replaceAll(this.array.toString(), "'", '"');
            this.ArrValue = JSON.parse(this.array);
        }else { this.ArrValue = []; }
        if (this.ArrValue.length > 0) {
            this.type = 'select';
        }
        $(this.elRef.nativeElement).addClass('header-filter');
        const strSessionRandom = this.replaceAll(this.GetNewID(), '-', '');
        let strHTML = '<a data-toggle="popover" for="pnPopover' + strSessionRandom + '" class="fa fa-filter font-green"></a>\
                <div class="hide" id="pnPopover' + strSessionRandom + '"><div class="form-group"><i class="fa fa-times-circle-o icon-close"></i>\
                <div align="center" class="caption-subject font-blue-sharp bold uppercase">FILTER MENU</div>';

        if (this.type === 'string') {
            strHTML = strHTML + '<div class="input-group">\
                                            <input type="text" class="form-control" placeholder="'+ this.label + '">\
                                            <span class="input-group-addon" style="cursor:pointer;"><i class="fa fa-search"></i></span>\
                                    </div>';
        }else if (this.type === 'select') {
            strHTML = strHTML + '<label class="control-label">' + this.label + '</label><div class="input-group">';
            strHTML = strHTML + '<select multiple class="form-control select2-allow-clear select2-hidden-accessible">';
            for (let x = 0; x < this.ArrValue.length; x++) {
                const item: any = this.ArrValue[x];
                if (item.value !== undefined) {
                    strHTML = strHTML + '<option value="' + item.value + '">' + item.text + '</option>';
                }else {
                    strHTML = strHTML + '<option value="' + this.ArrValue[x] + '">' + this.ArrValue[x] + '</option>';
                }
            }
            strHTML = strHTML + '</select><span class="input-group-addon" style="cursor:pointer;"><i class="fa fa-search"></i></span></div>';
        }
        strHTML = strHTML + '</div><div class="input-group pull-right" style="height:55px;"><a class="btn btn-sm red btn-outline btnClearFilter" ' +
            'style="margin-top:10px;"><span class="fa fa-remove" style="    margin-right: 5px;"></span><span>Clear Filter</span></a></div></div>';
        $(this.elRef.nativeElement).html(strHTML);
        let strPlacement = 'right';
        const offset = $(this.elRef.nativeElement).find('[data-toggle=popover]').offset();
        if (offset.left > (window.innerWidth / 2)) {
            strPlacement = 'left';
        }
        $(this.elRef.nativeElement).find('[data-toggle=popover]').popover({
            html: true,
            placement: strPlacement,
            content: function() {
                const id = $(this).attr('for');
                return $('#' + id).html();
            }
        }).click(() => {
            $(this.elRef.nativeElement).find('.popover .btnClearFilter').click(() => {
                const item: any = {};
                eval('item.' + this.outPara + '={}');
                eval('item.' + this.outPara + '.' + this.fieldName + '=""');
                this.callback(item);
                $(this.elRef.nativeElement).find('.popover').remove();
            });
            $(this.elRef.nativeElement).find('.popover .icon-close').click(() => {
                $(this.elRef.nativeElement).find('.popover').remove();
            });
            $(this.elRef.nativeElement).find('.popover input[type="text"]').keypress((event: any) => {
                if (event.which === 13) {
                    this.bindOutPut();
                }
            });
            $(this.elRef.nativeElement).find('.popover .input-group-addon').click(() => {
                this.bindOutPut();
            });
            const intWidth = $(this.elRef.nativeElement).find('.popover').width();
            // data-width='100%'
            $(this.elRef.nativeElement).find('.popover select').selectpicker({ iconBase: 'fa', tickIcon: 'fa-check' });
            $(this.elRef.nativeElement).find('.popover .bootstrap-select').css({ 'width': (intWidth - 64) + 'px' });
            // .css({'width':intWidth+'px'})
        });
    }
}
