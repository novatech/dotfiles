package my.crud.api.endpoint;

import my.crud.api.exception.ResourceNotFoundException;
import my.crud.api.model.Note;
import my.crud.api.security.WebSecurityConfig;
import my.crud.api.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({NoteController.class})
@Import({WebSecurityConfig.class})
class NoteControllerTest extends BaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Test
    void create() throws Exception {
        Note note = new Note();
        note.setTitle("note title");
        note.setContent("note content");
        when(noteService.create(any())).thenReturn(note);

        mockMvc.perform(post("/notes")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(toJson(note)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(note.getTitle())))
                .andExpect(jsonPath("$.content", is(note.getContent())));
    }

    @Test
    void readFound() throws Exception {
        Note note = new Note();
        note.setId(10L);
        note.setTitle("note title");
        note.setContent("note content");
        when(noteService.create(any())).thenReturn(note);

        given(noteService.read(note.getId())).willReturn(note);

        mockMvc.perform(get("/notes/" + note.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(note.getTitle())))
                .andExpect(jsonPath("$.content", is(note.getContent())));
    }

    @Test
    void readNotFound() throws Exception {
        given(noteService.read(100L)).willThrow(new ResourceNotFoundException("Cannot find note"));

        mockMvc.perform(get("/notes/" + 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.error", is("ResourceNotFoundException")));
    }

    @Test
    void updateFound() throws Exception {
        Note note = new Note();
        note.setId(99L);
        note.setTitle("note title");
        note.setContent("note content");

        Note noteUpdated = new Note();
        noteUpdated.setTitle("update:note title");
        noteUpdated.setContent("update:note content");

        given(noteService.update(eq(note.getId()), any())).willReturn(note);

        mockMvc.perform(put("/notes/" + note.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(toJson(noteUpdated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(note.getTitle())))
                .andExpect(jsonPath("$.content", is(note.getContent())));
    }

    @Test
    void updateNotFound() throws Exception {
        Note note = new Note();
        note.setTitle("note title");
        note.setContent("note content");

        given(noteService.update(any(), any())).willThrow(new ResourceNotFoundException("Cannot find note"));

        mockMvc.perform(put("/notes/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(toJson(note)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.error", is("ResourceNotFoundException")))
                .andExpect(jsonPath("$.details", hasItem("Cannot find note")));
    }


    @Test
    void deleteFound() throws Exception {
        Note note = new Note();
        note.setId(99L);
        note.setTitle("note title");
        note.setContent("note content");
        given(noteService.delete(note.getId())).willReturn(note);

        mockMvc.perform(delete("/notes/" + note.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(note.getTitle())))
                .andExpect(jsonPath("$.content", is(note.getContent())));
    }

    @Test
    void deleteNotFound() throws Exception {
        given(noteService.delete(100L)).willThrow(new ResourceNotFoundException("Cannot find note"));

        mockMvc.perform(delete("/notes/" + 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.error", is("ResourceNotFoundException")))
                .andExpect(jsonPath("$.details", hasItem("Cannot find note")));
    }

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}