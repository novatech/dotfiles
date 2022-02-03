package my.crud.api.service;

import my.crud.api.exception.ResourceNotFoundException;
import my.crud.api.model.Note;
import my.crud.api.model.NoteEntity;
import my.crud.api.repository.NoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final ModelMapper modelMapper;
    private static final String ERROR_TEXT = "Cannot find note";

    @Autowired
    public NoteService(NoteRepository noteRepository, ModelMapper modelMapper) {
        this.noteRepository = noteRepository;
        this.modelMapper = modelMapper;
    }

    public Note create(Note note) {
        NoteEntity noteEntity = new NoteEntity().setTitle(note.getTitle()).setContent(note.getContent());
        return modelMapper.map(noteRepository.save(noteEntity), Note.class);
    }

    public Note read(Long id) {
        return noteRepository.findById(id).map(n -> modelMapper.map(n, Note.class)).orElseThrow(() -> new ResourceNotFoundException(ERROR_TEXT));
    }

    public Note update(Long id, Note note) {
        NoteEntity noteToUpdate = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ERROR_TEXT));
        noteToUpdate.setTitle(note.getTitle());
        noteToUpdate.setContent(note.getContent());
        return modelMapper.map(noteRepository.save(noteToUpdate), Note.class);
    }

    public Note delete(Long id) {
        NoteEntity noteToDelete = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ERROR_TEXT));
        noteRepository.deleteById(id);
        return modelMapper.map(noteToDelete, Note.class);
    }

    public List<Note> list() {
        return noteRepository.findAll().stream().map(n -> modelMapper.map(n, Note.class)).collect(Collectors.toList());
    }
}
