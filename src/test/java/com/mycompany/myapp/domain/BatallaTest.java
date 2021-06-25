package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BatallaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Batalla.class);
        Batalla batalla1 = new Batalla();
        batalla1.setId(1L);
        Batalla batalla2 = new Batalla();
        batalla2.setId(batalla1.getId());
        assertThat(batalla1).isEqualTo(batalla2);
        batalla2.setId(2L);
        assertThat(batalla1).isNotEqualTo(batalla2);
        batalla1.setId(null);
        assertThat(batalla1).isNotEqualTo(batalla2);
    }
}
