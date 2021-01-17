package com.sharaf.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sharaf.sales.web.rest.TestUtil;

public class SaleInvoiceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleInvoice.class);
        SaleInvoice saleInvoice1 = new SaleInvoice();
        saleInvoice1.setId(1L);
        SaleInvoice saleInvoice2 = new SaleInvoice();
        saleInvoice2.setId(saleInvoice1.getId());
        assertThat(saleInvoice1).isEqualTo(saleInvoice2);
        saleInvoice2.setId(2L);
        assertThat(saleInvoice1).isNotEqualTo(saleInvoice2);
        saleInvoice1.setId(null);
        assertThat(saleInvoice1).isNotEqualTo(saleInvoice2);
    }
}
