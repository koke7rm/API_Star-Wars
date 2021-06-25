package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonajeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personaje.class);
        Personaje personaje1 = new Personaje();
        personaje1.setId(1L);
        Personaje personaje2 = new Personaje();
        personaje2.setId(personaje1.getId());
        assertThat(personaje1).isEqualTo(personaje2);
        personaje2.setId(2L);
        assertThat(personaje1).isNotEqualTo(personaje2);
        personaje1.setId(null);
        assertThat(personaje1).isNotEqualTo(personaje2);
    }
}
