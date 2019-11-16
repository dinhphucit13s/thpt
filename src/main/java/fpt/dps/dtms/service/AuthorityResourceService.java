package fpt.dps.dtms.service;

import fpt.dps.dtms.domain.AuthorityResource;
import fpt.dps.dtms.repository.AuthorityResourceRepository;
import fpt.dps.dtms.repository.search.AuthorityResourceSearchRepository;
import fpt.dps.dtms.service.dto.AuthorityResourceDTO;
import fpt.dps.dtms.service.mapper.AuthorityResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing AuthorityResource.
 */
@Service
@Transactional
public class AuthorityResourceService {

    private final Logger log = LoggerFactory.getLogger(AuthorityResourceService.class);

    private final AuthorityResourceRepository authorityResourceRepository;

    private final AuthorityResourceMapper authorityResourceMapper;

    private final AuthorityResourceSearchRepository authorityResourceSearchRepository;

    public AuthorityResourceService(AuthorityResourceRepository authorityResourceRepository, AuthorityResourceMapper authorityResourceMapper, AuthorityResourceSearchRepository authorityResourceSearchRepository) {
        this.authorityResourceRepository = authorityResourceRepository;
        this.authorityResourceMapper = authorityResourceMapper;
        this.authorityResourceSearchRepository = authorityResourceSearchRepository;
    }

    /**
     * Save a authorityResource.
     *
     * @param authorityResourceDTO the entity to save
     * @return the persisted entity
     */
    public AuthorityResourceDTO save(AuthorityResourceDTO authorityResourceDTO) {
        log.debug("Request to save AuthorityResource : {}", authorityResourceDTO);
        AuthorityResource authorityResource = authorityResourceMapper.toEntity(authorityResourceDTO);
        authorityResource = authorityResourceRepository.save(authorityResource);
        AuthorityResourceDTO result = authorityResourceMapper.toDto(authorityResource);
        authorityResourceSearchRepository.save(authorityResource);
        return result;
    }

    /**
     * Get all the authorityResources.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AuthorityResourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AuthorityResources");
        return authorityResourceRepository.findAll(pageable)
            .map(authorityResourceMapper::toDto);
    }

    /**
     * Get one authorityResource by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public AuthorityResourceDTO findOne(Long id) {
        log.debug("Request to get AuthorityResource : {}", id);
        AuthorityResource authorityResource = authorityResourceRepository.findOne(id);
        return authorityResourceMapper.toDto(authorityResource);
    }

    /**
     * Delete the authorityResource by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AuthorityResource : {}", id);
        authorityResourceRepository.delete(id);
        authorityResourceSearchRepository.delete(id);
    }

    /**
     * Search for the authorityResource corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AuthorityResourceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AuthorityResources for query {}", query);
        Page<AuthorityResource> result = authorityResourceSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(authorityResourceMapper::toDto);
    }
}
