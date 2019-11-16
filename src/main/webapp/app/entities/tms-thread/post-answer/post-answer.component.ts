import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CommentsService} from '../../comments';
import {TmsPost} from '../../tms-post';
import {JhiEventManager} from 'ng-jhipster';
import {Subscription} from 'rxjs/Subscription';
import {NgxSpinnerService} from 'ngx-spinner';

declare var jquery: any;
declare var $: any;

@Component({
  selector: 'jhi-post-answer',
  templateUrl: './post-answer.component.html',
  styleUrls: ['./post-answer.component.css']
})

export class PostAnswerComponent implements OnInit {
  @Output() detectChangeMainAnswer = new EventEmitter<any[]>();
  @Input() posts: any[];
  @Input() isClosed: boolean;
  @Input() currentUserLogin: any;

    eventSubscriber: Subscription;
  constructor(private commentService: CommentsService, private eventManager: JhiEventManager, private spinnerService: NgxSpinnerService) {
  }

  ngOnInit() {
      this.registerChangeInComments();
      this.registerUpdatePostInTmsThreads();
  }

  autoResizeCommentTextArea(element: any) {
    $(element).height(5);
    $(element).height($(element).prop('scrollHeight'));
  }

  showListCommentOfPost(post: any) {
    if (post.commentsDTOs.length > 0) {
       return;
    }
    this.loadListComment(post);
  }

  loadListComment(post: any) {
    this.commentService.getCommentByPostId(post.id).subscribe( (res) => {
      console.log(res.body);
      post.commentsDTOs = res.body;
      console.log(post);
    });
  }

  showSideComment(post: any) {
    post.isComment = true;
  }

  hideSideComment(post: any) {
    post.isComment = false;
    post.contentComment = '';
    post.attachmentsComment = new Array();
  }

  onFileChange(event, post: any) {
    console.log(event.target.files);
    let listFiles: any[];
    if (!post.attachmentsComment) {
      listFiles = new Array();
    } else {
      listFiles = post.attachmentsComment;
    }
    if (event.target.files && event.target.files.length) {
      for (let i = 0; i < event.target.files.length; i++) {
        const reader = new FileReader();
        const file = event.target.files[i];
        reader.readAsDataURL(file);
        reader.onloadend = () => {
          const fileReader = {
            filename: file.name,
            fileType: file.type,
            diskFile: '',
            value: reader.result.split(',')[1],
          };
          listFiles.push(fileReader);
        };
      }
    }
    post.attachmentsComment = listFiles;
    console.log(post);
  }

  saveComment(post: any) {
    console.log(post);
    if (!post.contentComment || post.contentComment.replace(/ /g, '') === '') {
      return;
    }

    this.spinnerService.show();
    const comment = {
      content : post.contentComment,
      attachments: post.attachmentsComment,
      postId: post.id
    };

    const formData: FormData = new FormData();
    formData.append('comments', new Blob([JSON.stringify(comment)], {
      type: 'application/json'
    }));

    this.commentService.createCommentOfPosts(formData).subscribe( (res) => {
      post.contentComment = null;
      post.attachmentsComment = new Array();
      post.comments++;
      this.loadListComment(post);
        this.spinnerService.hide();
    });
  }

  formatDate(date) {
    return new Date(date).toLocaleString();
  }

  cloneComment(comment: any) {
    const commentClone = JSON.parse(JSON.stringify(comment));
    comment.commentClone = commentClone;
  }

  removeCloneComment(comment: any) {
    delete comment.commentClone;
  }

  updateComment(comment: any, post: TmsPost) {
    console.log(comment.commentClone);
      this.spinnerService.show();
    const formData: FormData = new FormData();
    formData.append('comments', new Blob([JSON.stringify(comment.commentClone)], {
      type: 'application/json'
    }));
    this.commentService.updateComments(formData).subscribe( (res) => {
      delete comment.commentClone;
      const commentResponse = res.body;
      const index = post.commentsDTOs.findIndex((x) => x.id === commentResponse.id);
      if (index >= 0) {
        post.commentsDTOs[index] = commentResponse;
      }
        this.spinnerService.hide();
    });
  }

  onFileChangeComment(event, comment: any) {
    console.log(event.target.files);
    if (!comment.attachmentsAppend) {
      comment.attachmentsAppend = new Array();
    }
    if (!comment.attachments) {
      comment.attachments = new Array();
    }
    if (event.target.files && event.target.files.length) {
      for (let i = 0; i < event.target.files.length; i++) {
        const reader = new FileReader();
        const file = event.target.files[i];
        reader.readAsDataURL(file);
        reader.onloadend = () => {
          const fileReader = {
            filename: file.name,
            fileType: file.type,
            diskFile: '',
            value: reader.result.split(',')[1],
          };
          comment.attachments.push(fileReader);
          comment.attachmentsAppend.push(fileReader);
        };
      }
    }
    console.log(comment);
  }

  removeAttachment(attach, comment: any) {
    const objX = comment.attachments.filter((x) => x !== attach);
    comment.attachments = objX;

    if (!comment.attachmentsRemove) {
      comment.attachmentsRemove = new Array();
    }

    if (!comment.attachmentsAppend) {
      comment.attachmentsAppend = new Array();
    }
    const objRemove = comment.attachmentsAppend.find((x) => x === attach);

    if (!objRemove) {
      comment.attachmentsRemove.push(attach);
    }
    const objAppendX  = comment.attachmentsAppend.filter((x) => x !== attach);
    comment.attachmentsAppend = objAppendX;
    console.log(comment);
  }

    removeAttachmentCommentNew(attach, post: any) {
        console.log(post.attachmentsComment);
        const objX = post.attachmentsComment.filter(x => x !== attach);
        post.attachmentsComment = objX;
    }

    registerChangeInComments() {
        this.eventSubscriber = this.eventManager.subscribe('commentsListModification', (response) => {
            console.log(response);
            const idComment = response.id;
            this.posts.map((post) => {
                const comments = post.commentsDTOs;
                const index = comments.findIndex((x) => x.id === idComment);
                if (index >= 0) {
                    this.loadListComment(post);
                    post.comments--;
                }
                return post;
            });
        });
    }

    registerUpdatePostInTmsThreads() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tmsPostInThreadUpdateModification',
            (response) => {
                const newPost = response.tmsPost;
                const index = this.posts.findIndex((x) => x.id === newPost.id);
                this.posts[index] = newPost;
                console.log(this.posts);
            }
        );
    }
}
