import { Observable } from 'rxjs/Observable';
import { Ng2ImgMaxService } from 'ng2-img-max';
import { ImgCropService } from './img-crop.service';
export declare class ImgResizeExactService {
    private ng2ImgMaxService;
    private imgCropService;
    constructor(ng2ImgMaxService: Ng2ImgMaxService, imgCropService: ImgCropService);
    resizeExactFill(file: File, toWidth: number, toHeight: number, fillColor?: string): Observable<any>;
    resizeExactCrop(file: File, toWidth: number, toHeight: number): Observable<any>;
    private isImgUsingAlpha(imageData);
    private generateResultFile(blob, name, type, lastModified);
    private blobToFile(blob, name, lastModified);
}
