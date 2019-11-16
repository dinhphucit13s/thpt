import { Observable } from 'rxjs/Observable';
import { Ng2ImgMaxService } from 'ng2-img-max';
export declare class ImgCropService {
    private ng2ImgMaxService;
    constructor(ng2ImgMaxService: Ng2ImgMaxService);
    cropImage(file: File, toWidth: number, toHeight: number, startX?: number, startY?: number): Observable<any>;
    private isImgUsingAlpha(imageData);
    private generateResultFile(blob, name, type, lastModified);
    private blobToFile(blob, name, lastModified);
}
