package fr.it_akademy_book.service.impl;

import fr.it_akademy_book.domain.Author;
import fr.it_akademy_book.repository.AuthorRepository;
import fr.it_akademy_book.service.AuthorService;
import fr.it_akademy_book.service.dto.AuthorDTO;
import fr.it_akademy_book.service.mapper.AuthorMapper;
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
 * Service Implementation for managing {@link fr.it_akademy_book.domain.Author}.
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorDTO save(AuthorDTO authorDTO) {
        log.debug("Request to save Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    @Override
    public AuthorDTO update(AuthorDTO authorDTO) {
        log.debug("Request to update Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    @Override
    public Optional<AuthorDTO> partialUpdate(AuthorDTO authorDTO) {
        log.debug("Request to partially update Author : {}", authorDTO);

        return authorRepository
            .findById(authorDTO.getId())
            .map(existingAuthor -> {
                authorMapper.partialUpdate(existingAuthor, authorDTO);

                return existingAuthor;
            })
            .map(authorRepository::save)
            .map(authorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> findAll() {
        log.debug("Request to get all Authors");
        return authorRepository.findAll().stream().map(authorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the authors where Book is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorDTO> findAllWhereBookIsNull() {
        log.debug("Request to get all authors where Book is null");
        return StreamSupport
            .stream(authorRepository.findAll().spliterator(), false)
            .filter(author -> author.getBook() == null)
            .map(authorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorDTO> findOne(Long id) {
        log.debug("Request to get Author : {}", id);
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Author : {}", id);
        authorRepository.deleteById(id);
    }
}
