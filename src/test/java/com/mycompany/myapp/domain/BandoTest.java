package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BandoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bando.class);
        Bando bando1 = new Bando();
        bando1.setId(1L);
        Bando bando2 = new Bando();
        bando2.setId(bando1.getId());
        assertThat(bando1).isEqualTo(bando2);
        bando2.setId(2L);
        assertThat(bando1).isNotEqualTo(bando2);
        bando1.setId(null);
        assertThat(bando1).isNotEqualTo(bando2);
    }
}
