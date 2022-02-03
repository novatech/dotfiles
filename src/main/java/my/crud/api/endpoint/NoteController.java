package my.crud.api.endpoint;

import my.crud.api.exception.ResourceNotFoundException;
import my.crud.api.model.Note;
import my.crud.api.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/notes", produces = MediaType.APPLICATION_JSON_VALUE)
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Note> create(@Valid @RequestBody Note note) {
        Note createdNote = noteService.create(note);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdNote.getId()).toUri();
        return ResponseEntity.created(uri).body(createdNote);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Note read(@PathVariable Long id) throws ResourceNotFoundException {
        return noteService.read(id);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Note update(@PathVariable(value = "id") Long id, @RequestBody Note note) {
        return noteService.update(id, note);
    }

    @DeleteMapping(path = "/{id}")
    public Note delete(@PathVariable Long id) throws ResourceNotFoundException {
        return noteService.delete(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Note>> list() {
        return ResponseEntity.ok().body(noteService.list());
    }

}
