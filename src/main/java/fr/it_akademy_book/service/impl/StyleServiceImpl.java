package fr.it_akademy_book.service.impl;

import fr.it_akademy_book.domain.Style;
import fr.it_akademy_book.repository.StyleRepository;
import fr.it_akademy_book.service.StyleService;
import fr.it_akademy_book.service.dto.StyleDTO;
import fr.it_akademy_book.service.mapper.StyleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.it_akademy_book.domain.Style}.
 */
@Service
@Transactional
public class StyleServiceImpl implements StyleService {

    private final Logger log = LoggerFactory.getLogger(StyleServiceImpl.class);

    private final StyleRepository styleRepository;

    private final StyleMapper styleMapper;

    public StyleServiceImpl(StyleRepository styleRepository, StyleMapper styleMapper) {
        this.styleRepository = styleRepository;
        this.styleMapper = styleMapper;
    }

    @Override
    public StyleDTO save(StyleDTO styleDTO) {
        log.debug("Request to save Style : {}", styleDTO);
        Style style = styleMapper.toEntity(styleDTO);
        style = styleRepository.save(style);
        return styleMapper.toDto(style);
    }

    @Override
    public StyleDTO update(StyleDTO styleDTO) {
        log.debug("Request to update Style : {}", styleDTO);
        Style style = styleMapper.toEntity(styleDTO);
        style = styleRepository.save(style);
        return styleMapper.toDto(style);
    }

    @Override
    public Optional<StyleDTO> partialUpdate(StyleDTO styleDTO) {
        log.debug("Request to partially update Style : {}", styleDTO);

        return styleRepository
            .findById(styleDTO.getId())
            .map(existingStyle -> {
                styleMapper.partialUpdate(existingStyle, styleDTO);

                return existingStyle;
            })
            .map(styleRepository::save)
            .map(styleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StyleDTO> findAll() {
        log.debug("Request to get all Styles");
        return styleRepository.findAll().stream().map(styleMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StyleDTO> findOne(Long id) {
        log.debug("Request to get Style : {}", id);
        return styleRepository.findById(id).map(styleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Style : {}", id);
        styleRepository.deleteById(id);
    }
}
