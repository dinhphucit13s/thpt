package fpt.dps.dtms.web.rest.external;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.mysql.fabric.xmlrpc.base.Array;

import fpt.dps.dtms.domain.TaskTrackingTime;
import fpt.dps.dtms.service.BugsQueryService;
import fpt.dps.dtms.service.BugsService;
import fpt.dps.dtms.service.TaskTrackingTimeService;
import fpt.dps.dtms.service.dto.BugListDefaultDTO;
import fpt.dps.dtms.service.dto.BugsCriteria;
import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.ProjectBugListDefaultDTO;
import fpt.dps.dtms.web.rest.errors.BadRequestAlertException;
import fpt.dps.dtms.web.rest.util.HeaderUtil;
import fpt.dps.dtms.web.rest.util.PaginationUtil;
import fpt.dps.dtms.web.rest.vm.external.LogBugVM;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Bugs.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalTaskTrackingTimeResource {

    private final Logger log = LoggerFactory.getLogger(ExternalTaskTrackingTimeResource.class);

    private static final String ENTITY_NAME = "tasktrackingtime";

    private final TaskTrackingTimeService taskTrackingTimeService;


    public ExternalTaskTrackingTimeResource(TaskTrackingTimeService taskTrackingTimeService) {
        this.taskTrackingTimeService = taskTrackingTimeService;
    }

    /**
     * GET  /taskTrackingTime : get taskTrackingTime.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the taskTrackingTime in body
     */
    @GetMapping("/task-tracking-times")
    @Timed
    public ResponseEntity<TaskTrackingTime> findTaskTrackingByRole(@RequestParam(value = "taskId", required = false) Long taskId) {
        log.debug("REST request to get a page of TaskTrackingTimes");
        TaskTrackingTime track = taskTrackingTimeService.findTaskTrackingByRole(taskId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(track));
    }
    
    /**
     * GET  /taskTrackingTime : get taskTrackingTime before of Task by other userLogin.
     * @return the ResponseEntity with status 200 (OK) and the taskTrackingTime in body
     */
    @GetMapping("/task-tracking-times/before")
    @Timed
    public ResponseEntity<TaskTrackingTime> findTaskTrackingBeforeByTaskIdAndUserLogin(@RequestParam(value = "taskId", required = false) Long taskId, @RequestParam(value = "userLogin", required = false) String userLogin) {
        log.debug("REST request to get a TaskTrackingTimes");
        TaskTrackingTime track = taskTrackingTimeService.findTaskTrackingBeforeByTaskIdAndUserLogin(taskId, userLogin);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(track));
    }
}
