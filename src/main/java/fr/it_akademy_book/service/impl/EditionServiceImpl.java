package fr.it_akademy_book.service.impl;

import fr.it_akademy_book.domain.Edition;
import fr.it_akademy_book.repository.EditionRepository;
import fr.it_akademy_book.service.EditionService;
import fr.it_akademy_book.service.dto.EditionDTO;
import fr.it_akademy_book.service.mapper.EditionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.it_akademy_book.domain.Edition}.
 */
@Service
@Transactional
public class EditionServiceImpl implements EditionService {

    private final Logger log = LoggerFactory.getLogger(EditionServiceImpl.class);

    private final EditionRepository editionRepository;

    private final EditionMapper editionMapper;

    public EditionServiceImpl(EditionRepository editionRepository, EditionMapper editionMapper) {
        this.editionRepository = editionRepository;
        this.editionMapper = editionMapper;
    }

    @Override
    public EditionDTO save(EditionDTO editionDTO) {
        log.debug("Request to save Edition : {}", editionDTO);
        Edition edition = editionMapper.toEntity(editionDTO);
        edition = editionRepository.save(edition);
        return editionMapper.toDto(edition);
    }

    @Override
    public EditionDTO update(EditionDTO editionDTO) {
        log.debug("Request to update Edition : {}", editionDTO);
        Edition edition = editionMapper.toEntity(editionDTO);
        edition = editionRepository.save(edition);
        return editionMapper.toDto(edition);
    }

    @Override
    public Optional<EditionDTO> partialUpdate(EditionDTO editionDTO) {
        log.debug("Request to partially update Edition : {}", editionDTO);

        return editionRepository
            .findById(editionDTO.getId())
            .map(existingEdition -> {
                editionMapper.partialUpdate(existingEdition, editionDTO);

                return existingEdition;
            })
            .map(editionRepository::save)
            .map(editionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EditionDTO> findAll() {
        log.debug("Request to get all Editions");
        return editionRepository.findAll().stream().map(editionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the editions where Book is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EditionDTO> findAllWhereBookIsNull() {
        log.debug("Request to get all editions where Book is null");
        return StreamSupport
            .stream(editionRepository.findAll().spliterator(), false)
            .filter(edition -> edition.getBook() == null)
            .map(editionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EditionDTO> findOne(Long id) {
        log.debug("Request to get Edition : {}", id);
        return editionRepository.findById(id).map(editionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Edition : {}", id);
        editionRepository.deleteById(id);
    }
}
