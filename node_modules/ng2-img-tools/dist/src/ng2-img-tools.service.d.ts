import { Observable } from 'rxjs/Observable';
import { Ng2ImgMaxService } from 'ng2-img-max';
import { ImgCropService } from './img-crop.service';
import { ImgResizeExactService } from './img-resize-exact.service';
export declare class Ng2ImgToolsService {
    private imgResizeExactService;
    private ng2ImgMaxService;
    private imgCropService;
    constructor(imgResizeExactService: ImgResizeExactService, ng2ImgMaxService: Ng2ImgMaxService, imgCropService: ImgCropService);
    compress(files: File[], maxSizeInMB: number, ignoreAlpha?: boolean, logExecutionTime?: boolean): Observable<any>;
    resize(files: File[], maxWidth: number, maxHeight: number, logExecutionTime?: boolean): Observable<any>;
    crop(files: File[], toWidth: number, toHeight: number, startX?: number, startY?: number): Observable<any>;
    resizeExactCrop(files: File[], toWidth: number, toHeight: number): Observable<any>;
    resizeExactFill(files: File[], toWidth: number, toHeight: number, fillColor?: string): Observable<any>;
    resizeExactFillImage(file: File, toWidth: number, toHeight: number, fillColor?: string): Observable<any>;
    resizeExactCropImage(file: File, toWidth: number, toHeight: number): Observable<any>;
    cropImage(file: File, toWidth: number, toHeight: number, startX?: number, startY?: number): Observable<any>;
    compressImage(file: File, maxSizeInMB: number, ignoreAlpha?: boolean, logExecutionTime?: boolean): Observable<any>;
    resizeImage(file: File, maxWidth: number, maxHeight: number, logExecutionTime?: boolean): Observable<any>;
    getEXIFOrientedImage(image: HTMLImageElement): Promise<HTMLImageElement>;
}
