package fr.it_akademy_book.web.rest;

import fr.it_akademy_book.repository.EditionRepository;
import fr.it_akademy_book.service.EditionService;
import fr.it_akademy_book.service.dto.EditionDTO;
import fr.it_akademy_book.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.it_akademy_book.domain.Edition}.
 */
@RestController
@RequestMapping("/api/editions")
public class EditionResource {

    private final Logger log = LoggerFactory.getLogger(EditionResource.class);

    private static final String ENTITY_NAME = "edition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EditionService editionService;

    private final EditionRepository editionRepository;

    public EditionResource(EditionService editionService, EditionRepository editionRepository) {
        this.editionService = editionService;
        this.editionRepository = editionRepository;
    }

    /**
     * {@code POST  /editions} : Create a new edition.
     *
     * @param editionDTO the editionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new editionDTO, or with status {@code 400 (Bad Request)} if the edition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EditionDTO> createEdition(@RequestBody EditionDTO editionDTO) throws URISyntaxException {
        log.debug("REST request to save Edition : {}", editionDTO);
        if (editionDTO.getId() != null) {
            throw new BadRequestAlertException("A new edition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EditionDTO result = editionService.save(editionDTO);
        return ResponseEntity
            .created(new URI("/api/editions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /editions/:id} : Updates an existing edition.
     *
     * @param id the id of the editionDTO to save.
     * @param editionDTO the editionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated editionDTO,
     * or with status {@code 400 (Bad Request)} if the editionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the editionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EditionDTO> updateEdition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EditionDTO editionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Edition : {}, {}", id, editionDTO);
        if (editionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, editionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!editionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EditionDTO result = editionService.update(editionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, editionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /editions/:id} : Partial updates given fields of an existing edition, field will ignore if it is null
     *
     * @param id the id of the editionDTO to save.
     * @param editionDTO the editionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated editionDTO,
     * or with status {@code 400 (Bad Request)} if the editionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the editionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the editionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EditionDTO> partialUpdateEdition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EditionDTO editionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Edition partially : {}, {}", id, editionDTO);
        if (editionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, editionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!editionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EditionDTO> result = editionService.partialUpdate(editionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, editionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /editions} : get all the editions.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of editions in body.
     */
    @GetMapping("")
    public List<EditionDTO> getAllEditions(@RequestParam(required = false) String filter) {
        if ("book-is-null".equals(filter)) {
            log.debug("REST request to get all Editions where book is null");
            return editionService.findAllWhereBookIsNull();
        }
        log.debug("REST request to get all Editions");
        return editionService.findAll();
    }

    /**
     * {@code GET  /editions/:id} : get the "id" edition.
     *
     * @param id the id of the editionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the editionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EditionDTO> getEdition(@PathVariable Long id) {
        log.debug("REST request to get Edition : {}", id);
        Optional<EditionDTO> editionDTO = editionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(editionDTO);
    }

    /**
     * {@code DELETE  /editions/:id} : delete the "id" edition.
     *
     * @param id the id of the editionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEdition(@PathVariable Long id) {
        log.debug("REST request to delete Edition : {}", id);
        editionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
